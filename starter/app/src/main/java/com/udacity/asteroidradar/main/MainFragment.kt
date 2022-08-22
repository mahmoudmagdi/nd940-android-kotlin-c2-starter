package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentMainBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val dataSource = AsteroidDataBase.getInstance(application).asteroidDataBaseDao
        val viewModelFactory = MainViewModelFactory(dataSource, application)
        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        val adapter = AsteroidsAdapter(AsteroidClickListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
            Toast.makeText(context, "${asteroid.id}", Toast.LENGTH_LONG).show()
        })

        with(binding) {
            mainViewModel = viewModel
            lifecycleOwner = this@MainFragment
            root.asteroid_recycler.adapter = adapter
        }
        viewModel.allAsteroids.observe(viewLifecycleOwner) {
            adapter.asteroidsList = it
        }
        viewModel.navigateToAsteroidDetails.observe(viewLifecycleOwner) { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.onAsteroidDetailsNavigated()
            }
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
