package com.example.alarmapp.ui.note

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmapp.databinding.NoteItemBinding


class NoteAdapter(private val wifiList: Map<String, Int>) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NoteItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        binding.videoView.minimumHeight = binding.videoView.width
        return ViewHolder(binding)
    }

    /**
     * Manage Video
     *
     * reference:
     * https://abhiandroid.com/ui/videoview
     * https://stackoverflow.com/a/8431374/7676003
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.ssid.text = wifiList.keys.elementAt(position)
            with(binding.videoView) {
                setOnPreparedListener {
                    it.isLooping = true
                }
                setVideoURI(uriAtIndex(holder.binding.root.context, position))
            }
            if (position == 0) {
                binding.videoView.start()
            }
        }
    }

    private fun uriAtIndex(context: Context, position: Int): Uri =
        Uri.parse("android.resource://${context.packageName}/${wifiList.values.elementAt(position)}")

    override fun getItemCount(): Int = wifiList.size
}