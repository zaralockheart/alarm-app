package com.example.alarmapp.ui.weatherforecast

import android.os.Bundle
import android.text.format.DateFormat.format
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.alarmapp.R
import com.example.alarmapp.databinding.FragmentWeatherForecastBinding
import java.text.SimpleDateFormat
import java.util.*

class WeatherForecastFragment : Fragment() {

    private lateinit var dashboardViewModel: WeatherForecastViewModel
    private var _binding: FragmentWeatherForecastBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this).get(WeatherForecastViewModel::class.java)

        _binding = FragmentWeatherForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendar = Calendar.getInstance(Locale.getDefault())
        binding.textView3.text = format("EEEE, MMMM d yyyy", calendar).toString()
        binding.textView4.text = format("HH:mm a", calendar).toString()

        dashboardViewModel.weathers.observe(viewLifecycleOwner) {
            val currentHour = Calendar.getInstance(Locale.getDefault()).get(Calendar.HOUR_OF_DAY)
            val targetHour =
                if (currentHour % 3 == 0) currentHour else if ((currentHour + 1) % 3 == 0) currentHour +1 else if ((currentHour+2) % 3 == 0) currentHour+2 else currentHour+3
            val filtered = it.list.filter { data ->
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val initCalendar = Calendar.getInstance(Locale.getDefault())
                initCalendar.time = formatter.parse(data.dt_txt)!!
                initCalendar.get(Calendar.HOUR_OF_DAY).toString() == targetHour.toString()
            }

            filtered.forEachIndexed { index, element ->
                val imageResource = when (element.weather.first().main.lowercase()) {
                    "clouds" -> R.drawable.ic_cloud
                    "rain" -> R.drawable.ic_heavy_rain
                    else -> R.drawable.ic_sun
                }
                val id = resources.getIdentifier("weatherImage$index", "id", context?.packageName)
                view.findViewById<ImageView>(id).setImageResource(imageResource)
            }

        }

        binding.button.setOnClickListener {
            dashboardViewModel.getWeather(binding.editTextLocation.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}