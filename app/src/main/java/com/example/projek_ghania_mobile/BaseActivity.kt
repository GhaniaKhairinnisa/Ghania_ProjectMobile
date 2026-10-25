package com.example.projek_ghania_mobile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.projek_ghania_mobile.Beranda.HomeFragment
import com.example.projek_ghania_mobile.Jelajahi.JelajahiFragment
import com.example.projek_ghania_mobile.Kalender.KalenderFragment
import com.example.projek_ghania_mobile.Profile.ProfileFragment
import com.example.projek_ghania_mobile.Tiket.TiketFragment
import com.example.projek_ghania_mobile.databinding.ActivityBaseBinding

class BaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_beranda -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_jelajahi -> {
                    replaceFragment(JelajahiFragment())
                    true
                }
                R.id.nav_kalender -> {
                    replaceFragment(KalenderFragment())
                    true
                }
                R.id.nav_tiket -> {
                    replaceFragment(TiketFragment())
                    true
                }
                R.id.nav_profil -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}