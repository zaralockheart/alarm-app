package com.example.alarmapp.ui.alarm

import android.R.color
import android.graphics.Color
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmapp.data.AlarmEntity
import com.example.alarmapp.databinding.AlarmItemBinding
import java.util.*

/**
 * Needed for RecyclerView
 *
 * reference:
 * https://developer.android.com/guide/topics/ui/layout/recyclerview?gclid=Cj0KCQjwktKFBhCkARIsAJeDT0jYEgs0ca0DYN_xiojdESU3lex1eeqcm0HHnCD3DSPvu_i7K0fy-8IaApbZEALw_wcB&gclsrc=aw.ds
 *
 * codelab:
 * https://developer.android.com/codelabs/kotlin-android-training-diffutil-databinding#0
 */
class AlarmAdapter :
    ListAdapter<AlarmEntity, AlarmAdapter.AlarmViewHolder>(AlarmComparator()) {

    var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder =
        AlarmViewHolder.create(parent)

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.switch1.setOnClickListener {
            onClickListener?.onItemClick(getItem(position), !holder.binding.switch1.isChecked)
        }
    }

    class AlarmViewHolder(val binding: AlarmItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: AlarmEntity) {
            val c = Calendar.getInstance()
            c[Calendar.HOUR_OF_DAY] = alarm.hour
            c[Calendar.MINUTE] = alarm.minute
            binding.switch1.text = DateFormat.format("HH : mm a", c).toString()
            binding.switch1.isChecked = alarm.enabled == 1
            binding.ahd.setBackgroundColor(getColor(alarm.sun ?: false, binding.root))
            binding.isn.setBackgroundColor(getColor(alarm.mon ?: false, binding.root))
            binding.sel.setBackgroundColor(getColor(alarm.tue ?: false, binding.root))
            binding.rab.setBackgroundColor(getColor(alarm.wed ?: false, binding.root))
            binding.kha.setBackgroundColor(getColor(alarm.thu ?: false, binding.root))
            binding.jum.setBackgroundColor(getColor(alarm.fri ?: false, binding.root))
            binding.sab.setBackgroundColor(getColor(alarm.sat ?: false, binding.root))
            
        }

        private fun getColor(selected: Boolean, view: View): Int = if (selected) view.context.resources.getColor(color.holo_purple) else Color.parseColor("#F5F5F5")

        companion object {
            fun create(parent: ViewGroup): AlarmViewHolder {
                val binding = AlarmItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                return AlarmViewHolder(binding)
            }
        }
    }

    class AlarmComparator : DiffUtil.ItemCallback<AlarmEntity>() {
        override fun areItemsTheSame(oldItem: AlarmEntity, newItem: AlarmEntity): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: AlarmEntity, newItem: AlarmEntity): Boolean =
            oldItem.uid == newItem.uid && oldItem.hour == newItem.hour && oldItem.minute == newItem.minute &&oldItem.enabled == newItem.enabled
    }

    fun setOnItemClickListener(listener: OnClickListener) {
        onClickListener = listener
    }

}

interface OnClickListener {
    fun onItemClick(alarm: AlarmEntity, state: Boolean)
}