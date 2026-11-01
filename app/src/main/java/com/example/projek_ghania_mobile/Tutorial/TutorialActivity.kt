package com.example.projek_ghania_mobile.Tutorial

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projek_ghania_mobile.LoginActivity
import com.example.projek_ghania_mobile.R
import com.example.projek_ghania_mobile.databinding.ActivityTutorialBinding

class TutorialActivity : AppCompatActivity() {
        private  lateinit var binding: ActivityTutorialBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()

            binding = ActivityTutorialBinding.inflate(layoutInflater)
            setContentView(binding.root)

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
            val fragmentsList = listOf(
                Tutorial1Fragment(),
                Tutorial2Fragment(),
                Tutorial3Fragment()
            )
            val adapter = TutorialFragmentAdapter(this, fragmentsList)
            binding.tutorialViewPager.adapter = adapter
            binding.dotIndicator.attachTo(binding.tutorialViewPager)

            binding.btnLewati.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
}

