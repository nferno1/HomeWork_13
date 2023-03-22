package nferno1.homework_13.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import nferno1.homework_13.databinding.FragmentMainBinding

@Suppress("DEPRECATION")
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var viewModel: MyMainViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            val textSearch: String = binding.searchImput.text.toString()

            binding.message.setText("По вашему запросу ничего не найдено <$textSearch>")
            viewModel.onSignInClick(textSearch)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state: State ->
                when (state) {
                    State.Loading -> {
                        binding.progressBar.isVisible = true
                        binding.button.isEnabled = false
                        binding.search.error = null
                        binding.searchImput.addTextChangedListener(textWatcher)
                    }
                    State.Success -> {
                        binding.progressBar.isVisible = false
                        binding.button.isEnabled = true
                        binding.search.error = null
                        binding.searchImput.addTextChangedListener(textWatcher)
                    }
                    is State.Error -> {
                        binding.button.isEnabled = false
                        binding.message.text = "Error:${state.msg}"
                        binding.search.error = state.msg
                        binding.searchImput.addTextChangedListener(textWatcher)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.error.collect {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun afterTextChanged(s: Editable?) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.button.isEnabled = s.toString().length > 3
        }


    }
}
