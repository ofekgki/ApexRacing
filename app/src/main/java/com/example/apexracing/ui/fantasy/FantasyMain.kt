package com.example.apexracing.ui.fantasy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.apexracing.R
import com.example.apexracing.adapters.FantasyPickAdapter
import com.example.apexracing.databinding.FragmentFantasyMainBinding
import com.example.apexracing.models.FantasyItem
import com.example.apexracing.models.User
import com.example.apexracing.models.UserIds
import com.example.apexracing.models.UserViewModel
import com.example.apexracing.models.UserViewModel.PickType
import com.example.apexracing.utilities.DBData
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

class FantasyMain : Fragment() {

    enum class Mode { DRIVERS, CONSTRUCTORS }

    private val storageRef = FirebaseStorage.getInstance().reference

    private lateinit var binding: FragmentFantasyMainBinding
    private val userVM: UserViewModel by activityViewModels()

    private lateinit var pickAdapter: FantasyPickAdapter

    private var mode: Mode = Mode.DRIVERS
    private lateinit var uid: String
    private lateinit var dto: UserIds


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFantasyMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        setupButtons()
        setupGridRemove()

        showDrivers()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userVM.state.collect { s ->
                    val ready = s as? UserViewModel.UserUiState.Ready ?: return@collect
                    val user = ready.user
                    uid = ready.uid
                    dto = ready.userIds

                    bindBudget(user)
                    bindGrid(user)
                }
            }
        }
    }

    private fun setupRecycler() {
        binding.fantasyRVDrivers.visibility = View.VISIBLE
        binding.fantasyRVConstructors.visibility = View.GONE

        pickAdapter = FantasyPickAdapter(
            onAddClicked = { item ->
                userVM.pickFantasyItem(item.type, item.id, item.price)
            }
        )

        binding.fantasyRVDrivers.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = pickAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupButtons() {
        binding.fantasyCHPDrivers.setOnClickListener { showDrivers() }
        binding.fantasyCHPConstructors.setOnClickListener { showTeams() }

        binding.fantasyBTNSave.setOnClickListener {
            userVM.saveUserIdsToRTDB(uid,dto)
        }
    }

    private fun showDrivers() {
        mode = Mode.DRIVERS

        val items = DBData.drivers.map { d ->
            FantasyItem(
                id = d.id,
                name = d.getFullName(),
                points = d.points,
                price = d.fantasyPrice,
                imageUrl = d.imgRef,
                type = PickType.DRIVER
            )
        }
        android.util.Log.d("FantasyMain", "drivers items size = ${items.size}")
        pickAdapter.submitList(items)
    }

    private fun showTeams() {
        mode = Mode.CONSTRUCTORS

        val items = DBData.teams.map { t ->
            FantasyItem(
                id = t.id,
                name = t.name,
                points = t.points,
                price = t.fantasyPrice,
                imageUrl = t.imgRef,
                type = PickType.CONSTRUCTOR
            )
        }

        pickAdapter.submitList(items)
    }

    private fun bindBudget(user: User) {
        binding.budgetLBLRemainingValue.text = "$${"%.1f".format(user.fantasyBudget)}"

        val pickedCount = user.fantasyDriver.size + user.fantasyConstructor.size
        binding.fantasyLBLAmount.text = "($pickedCount/5)"
        binding.budgetLBLUsed.text = "${user.getBudgetPercentage()}% Used"
        binding.budgetPRG.progress = user.getBudgetPercentage().toInt()
    }

    private fun bindGrid(user: User) {
        val d1 = user.fantasyDriver.getOrNull(0)
        val d2 = user.fantasyDriver.getOrNull(1)
        val d3 = user.fantasyDriver.getOrNull(2)

        val t1 = user.fantasyConstructor.getOrNull(0)
        val t2 = user.fantasyConstructor.getOrNull(1)

        loadSlot(binding.fantasyIMGDriver1, d1?.imgRef)
        loadSlot(binding.fantasyIMGDriver2, d2?.imgRef)
        loadSlot(binding.fantasyIMGDriver3, d3?.imgRef)

        loadSlot(binding.fantasyIMGTeam1, t1?.imgRef)
        loadSlot(binding.fantasyIMGTeam2, t2?.imgRef)
    }

    private fun loadSlot(img: ImageView, url: String?) {
        val path = url?.trim()?.removePrefix("/")?.takeIf { it.isNotBlank() }
            ?: run {
                img.setImageResource(R.drawable.thestig)
                img.scaleType =
                    if (mode == Mode.DRIVERS) ImageView.ScaleType.CENTER_CROP
                    else ImageView.ScaleType.FIT_CENTER
                return
            }

        storageRef.child(path).downloadUrl
            .addOnSuccessListener { uri ->
                if (!isAdded) return@addOnSuccessListener

                Glide.with(img)
                    .load(uri)
                    .placeholder(R.drawable.thestig)
                    .error(R.drawable.thestig)
                    .centerCrop()
                    .into(img)
            }
            .addOnFailureListener {
                img.setImageResource(R.drawable.thestig)
            }

        img.scaleType =
            if (mode == Mode.DRIVERS) ImageView.ScaleType.CENTER_CROP
            else ImageView.ScaleType.FIT_CENTER
    }

    private fun setupGridRemove() {
        binding.fantasyIMGDriver1.setOnLongClickListener { removeDriverAt(0); true }
        binding.fantasyIMGDriver2.setOnLongClickListener { removeDriverAt(1); true }
        binding.fantasyIMGDriver3.setOnLongClickListener { removeDriverAt(2); true }

        binding.fantasyIMGTeam1.setOnLongClickListener { removeTeamAt(0); true }
        binding.fantasyIMGTeam2.setOnLongClickListener { removeTeamAt(1); true }
    }

    private fun removeDriverAt(index: Int) {
        val ready = userVM.state.value as? UserViewModel.UserUiState.Ready ?: return
        val driver = ready.user.fantasyDriver.getOrNull(index) ?: return
        userVM.removeFantasyItem(PickType.DRIVER, driver.id, driver.fantasyPrice)
    }

    private fun removeTeamAt(index: Int) {
        val ready = userVM.state.value as? UserViewModel.UserUiState.Ready ?: return
        val team = ready.user.fantasyConstructor.getOrNull(index) ?: return
        userVM.removeFantasyItem(PickType.CONSTRUCTOR, team.id, team.fantasyPrice)
    }
}