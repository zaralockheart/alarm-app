package com.example.alarmapp.ui.alarm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmapp.AlarmApplication
import com.example.alarmapp.alarm.AlarmUtilities
import com.example.alarmapp.data.AlarmEntity
import com.example.alarmapp.databinding.FragmentHomeBinding
import com.example.alarmapp.ui.setalarm.SetAlarmActivity
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class AlarmFragment : Fragment(), OnClickListener {
    private val homeViewModel: AlarmViewModel by viewModels {
        AlarmViewModelFactory((activity?.application as AlarmApplication).repository)
    }
    private var _binding: FragmentHomeBinding? = null
    private var alarmAdapter: AlarmAdapter = AlarmAdapter()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alarmAdapter.setOnItemClickListener(this)
        alarmRecyclerView.adapter = this.alarmAdapter

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                loadData()
            }

        binding.buttonAdd.setOnClickListener {
            resultLauncher.launch(
                Intent(
                    this.activity,
                    SetAlarmActivity::class.java
                )
            )
        }

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.absoluteAdapterPosition
                homeViewModel.delete(alarmAdapter.currentList[position])
                alarmAdapter.notifyItemRemoved(position)
                AlarmUtilities.cancelAllAlarm(requireContext(), alarmAdapter.currentList[position].uid)
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(alarmRecyclerView)
        loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadData() {
        homeViewModel.allWords.observe(viewLifecycleOwner, {
            alarmRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                alarmAdapter.submitList(it)
            }
        })
    }


    override fun onItemClick(alarm: AlarmEntity, state: Boolean) {
        homeViewModel.update(alarm.copy(enabled = if (state) 0 else 1))
        if (state) {
            homeViewModel.update(alarm.copy(enabled = 0))
            AlarmUtilities.cancelAllAlarm(requireContext(), alarm.uid)
            return
        }
        val selectedTime = Calendar.getInstance()
        selectedTime.set(Calendar.HOUR_OF_DAY, alarm.hour)
        selectedTime.set(Calendar.MINUTE, alarm.minute)
        AlarmUtilities.setAlarm(requireContext(), alarm.uid, selectedTime)
    }
}