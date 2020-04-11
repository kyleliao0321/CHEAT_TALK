package com.example.cheat_talk.ui.fragments.loading

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.cheat_talk.databinding.LoadingFragmentBinding

class LoadingFragment: DialogFragment() {
    val MESSAGE_TAG: String = "com.example.cheat_talk.ui.fragment.loading.MESSAGE_TAG"
    private lateinit var message: String

    companion object {
        fun newInstance(message: String): LoadingFragment {
            val loadingFragment = LoadingFragment()
            val args = Bundle()
            args.putString(loadingFragment.MESSAGE_TAG, message)
            loadingFragment.arguments = args
            return loadingFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val binding = LoadingFragmentBinding.inflate(inflater, null, false)
        binding.message = requireArguments().getString(MESSAGE_TAG)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        builder.setCancelable(false)
        return builder.create()
    }
}