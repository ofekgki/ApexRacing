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
import com.example.apexracing.models.UserViewModel
import com.example.apexracing.models.UserViewModel.PickType
import com.example.apexracing.utilities.DBData
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

class FantasyMain : Fragment() {

    private enum class Mode { DRIVERS, CONSTRUCTORS }

    private lateinit var binding: FragmentFantasyMainBinding
    private val userVM: UserViewModel by activityViewModels()
    private val storageRef = FirebaseStorage.getInstance().reference

    private lateinit var pickAdapter: FantasyPickAdapter
    private var mode: Mode = Mode.DRIVERS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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

                    pickAdapter.setSelectedItems(
                        driverIds = ready.userIds.fantasyDriverIds,
                        constructorIds = ready.userIds.fantasyConstructorIds
                    )

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
                userVM.toggleFantasyItem(
                    type = item.type,
                    pickedId = item.id,
                    priceM = item.price ?: 0f
                )
            }
        )

        binding.fantasyRVDrivers.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = pickAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupButtons() {
        binding.fantasyCHPDrivers.setOnClickListener { showDrivers() }
        binding.fantasyCHPConstructors.setOnClickListener { showTeams() }

        binding.fantasyBTNSave.setOnClickListener {
            userVM.saveFantasyTeam()
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
        val budget = user.fantasyBudget ?: 0f
        binding.budgetLBLRemainingValue.text = "$${"%.1f".format(budget)}"

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

        loadSlot(binding.fantasyIMGDriver1, d1?.imgRef, true)
        loadSlot(binding.fantasyIMGDriver2, d2?.imgRef, true)
        loadSlot(binding.fantasyIMGDriver3, d3?.imgRef, true)

        loadSlot(binding.fantasyIMGTeam1, t1?.imgRef, false)
        loadSlot(binding.fantasyIMGTeam2, t2?.imgRef, false)
    }

    private fun loadSlot(img: ImageView, path: String?, isDriver: Boolean) {
        img.scaleType = if (isDriver) {
            ImageView.ScaleType.CENTER_CROP
        } else {
            ImageView.ScaleType.FIT_CENTER
        }

        val cleanPath = path?.trim()?.removePrefix("/")

        if (cleanPath.isNullOrBlank()) {
            img.setImageResource(R.drawable.user_profile_blank)
            return
        }

        storageRef.child(cleanPath).downloadUrl
            .addOnSuccessListener { uri ->
                if (!isAdded) return@addOnSuccessListener

                val request = Glide.with(img)
                    .load(uri)
                    .placeholder(R.drawable.user_profile_blank)
                    .error(R.drawable.user_profile_blank)

                if (isDriver) {
                    request.centerCrop().into(img)
                } else {
                    request.fitCenter().into(img)
                }

            }
            .addOnFailureListener {
                img.setImageResource(R.drawable.user_profile_blank)
            }
    }

    private fun setupGridRemove() {
        binding.fantasyIMGDriver1.setOnLongClickListener {
            removeDriverAt(0)
            true
        }
        binding.fantasyIMGDriver2.setOnLongClickListener {
            removeDriverAt(1)
            true
        }
        binding.fantasyIMGDriver3.setOnLongClickListener {
            removeDriverAt(2)
            true
        }

        binding.fantasyIMGTeam1.setOnLongClickListener {
            removeTeamAt(0)
            true
        }
        binding.fantasyIMGTeam2.setOnLongClickListener {
            removeTeamAt(1)
            true
        }
    }

    private fun removeDriverAt(index: Int) {
        val ready = userVM.state.value as? UserViewModel.UserUiState.Ready ?: return
        val driver = ready.user.fantasyDriver.getOrNull(index) ?: return

        userVM.removeFantasyItem(
            type = PickType.DRIVER,
            removeId = driver.id,
            priceM = driver.fantasyPrice ?: 0f
        )
    }

    private fun removeTeamAt(index: Int) {
        val ready = userVM.state.value as? UserViewModel.UserUiState.Ready ?: return
        val team = ready.user.fantasyConstructor.getOrNull(index) ?: return

        userVM.removeFantasyItem(
            type = PickType.CONSTRUCTOR,
            removeId = team.id,
            priceM = team.fantasyPrice ?: 0f
        )
    }
}