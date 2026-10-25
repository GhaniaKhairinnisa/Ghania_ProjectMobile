package com.example.projek_ghania_mobile.Beranda

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.example.projek_ghania_mobile.LoginActivity
import com.example.projek_ghania_mobile.R
import com.example.projek_ghania_mobile.databinding.ActivityHomeFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {
    private var _binding: ActivityHomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityHomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.inflateMenu(R.menu.menu_home)

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_logout -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Konfirmasi Logout")
                            .setMessage("Apakah Anda yakin ingin keluar dan kembali ke halaman Login?")
                            .setNegativeButton("Tidak") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .setPositiveButton("Ya") { dialog, _ ->
                                val sharedPref = requireContext().getSharedPreferences("user_pref", MODE_PRIVATE)
                                sharedPref.edit().clear().apply()

                                startActivity(Intent(requireContext(), LoginActivity::class.java))
                                requireActivity().finish()
                            }
                            .setCancelable(false)
                            .show()
                    true
                }
                R.id.action_notifikasi -> {
                    Snackbar.make(binding.root, "Menu Notifikasi diklik!", Snackbar.LENGTH_SHORT).show()
                    true
                }
                R.id.action_profile -> {
                    Snackbar.make(binding.root, "Menu Profil diklik!", Snackbar.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "Kreator")

        binding.tvSapaan.text = "Halo, $username! \uD83D\uDC4B"

        binding.btnLihatEvent.setOnClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}