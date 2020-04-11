package com.example.cheat_talk.ui.fragments.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cheat_talk.MainActivity
import com.example.cheat_talk.R
import kotlinx.coroutines.coroutineScope

class LaunchFragment : Fragment() {
    private lateinit var eventListener: LaunchFragmentEventListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        eventListener = (activity as MainActivity).launchFragmentEventListener

        val view: View = inflater.inflate(R.layout.launch_fragment, container, false)
        view.setOnClickListener(View.OnClickListener {
            eventListener.onFinishLaunching()
        })
        return view
    }
}