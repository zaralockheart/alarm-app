package com.example.alarmapp.ui.note

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmapp.R
import com.example.alarmapp.databinding.FragmentNotificationsBinding


class NoteFragment : Fragment() {

    private lateinit var noteViewModel: NoteViewModel
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val exerciseImages: Map<String, Int> = mapOf(
        "Glute Bridge" to R.raw.glute_bridge,
        "Side Plank" to R.raw.side_plank,
        "Standing Overhead Dumbbell" to R.raw.standing_overhead_dumbell,
        "Single Leg Deadlift" to R.raw.single_leg_deadlift,
        "Lunges" to R.raw.lunges,
        "Planks" to R.raw.planks,
        "Squats" to R.raw.squats,
        "Push Ups" to R.raw.push_ups,
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        noteViewModel =
            ViewModelProvider(this).get(NoteViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val noteAdapter = NoteAdapter(exerciseImages)

        binding.noteList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = noteAdapter
        }

        /**
         * Video only suppose to be played on active view. Disposed view will have to start again. And video
         * is removed.
         */
        binding.noteList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    val position = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (position > 0) {
                        val firstView =
                            (recyclerView.layoutManager as LinearLayoutManager).findViewByPosition(
                                position
                            )
                        firstView!!.findViewById<VideoView>(R.id.videoView)?.pause()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                if (position > 0) {
                    val firstView =
                        (recyclerView.layoutManager as LinearLayoutManager).findViewByPosition(
                            position
                        )
                    firstView!!.findViewById<VideoView>(R.id.videoView)?.start()
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}