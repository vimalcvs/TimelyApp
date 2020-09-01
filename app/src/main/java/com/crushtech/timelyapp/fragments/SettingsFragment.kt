package com.crushtech.timelyapp.fragments

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.crushtech.timelyapp.R
import com.crushtech.timelyapp.ui.MainActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsFragment : Fragment() {
    private var switch: SwitchMaterial? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //get switch state,save it into app preference
        val sharedprefs: SharedPreferences =
            requireContext().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val switchIsTurnedOn = sharedprefs.getBoolean("DARK MODE", false)
        if (switchIsTurnedOn) {
            //if true then change app theme to dark mode
            layoutInflater.context.setTheme(R.style.DarkMode)
        } else {
            layoutInflater.context.setTheme(R.style.WhiteMode)
        }

        val view = inflater.inflate(R.layout.fragment_settings, container, false)


        switch = view.findViewById(R.id.switchTheme)
        // the switch state is assigned to the boolean value gotten from shared prefs
        switch!!.isChecked = switchIsTurnedOn

        switch!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val editor = requireContext().getSharedPreferences(
                    "pref",
                    Context.MODE_PRIVATE
                )
                switch!!.isChecked = true
                editor.edit().putBoolean("DARK MODE", true).apply()
                restartApp()
            } else {
                switch!!.isChecked = false
                val editor = requireContext().getSharedPreferences(
                    "pref",
                    Context.MODE_PRIVATE
                )
                editor.edit().putBoolean("DARK MODE", false).apply()
                restartApp()

            }
        }
        //privacy policy
        view.findViewById<TextView>(R.id.PrivacyPolicy).setOnClickListener {
            showBrowser("http://www.crushtech.unaux.com/privacypolicy/?i=1")
        }
        //rate app
        view.findViewById<TextView>(R.id.RateApp).setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + requireContext().packageName)
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + requireContext().packageName)
                    )
                )
            }

        }

        //contact
        view.findViewById<TextView>(R.id.Contact).setOnClickListener {
            showBrowser("http://www.crushtech.unaux.com/contact/?i=1")
        }

        //share to
        view.findViewById<TextView>(R.id.Recommend).setOnClickListener {
            val a = Intent(Intent.ACTION_SEND)
            val appPackageName =
                requireContext().applicationContext.packageName
            val strAppLink: String
            strAppLink = try {
                "https://play.google.com/store/apps/details?id$appPackageName"
            } catch (anfe: ActivityNotFoundException) {
                "https://play.google.com/store/apps/details?id$appPackageName"
            }
            a.type = "text/link"
            val shareBody =
                "Hey, Check out TimelyApp, i use it to schedule my alarms. Get it for free at \n$strAppLink"
            val shareSub = "APP NAME/TITLE"
            a.putExtra(Intent.EXTRA_SUBJECT, shareSub)
            a.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(a, "Share Using"))

        }
        //show about app dialog
        view.findViewById<TextView>(R.id.AboutApp).setOnClickListener {
            showAboutAppDialog()
        }
        return view

    }

    //show dialog function
    private fun showAboutAppDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.about_app)
        val dismissDialog = dialog.findViewById<MaterialButton>(R.id.close_aboutapp_dialog)

        dismissDialog.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        dialog.setCancelable(false)
    }

    //our browser function
    private fun showBrowser(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    //restart the app after our switch action
    private fun restartApp() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
    }


    override fun onStart() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.settings)
        super.onStart()
    }
}