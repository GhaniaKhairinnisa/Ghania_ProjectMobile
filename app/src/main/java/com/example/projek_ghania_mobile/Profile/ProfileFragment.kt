package com.example.projek_ghania_mobile.Profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.projek_ghania_mobile.LoginActivity
import com.example.projek_ghania_mobile.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "Kreator")
        binding.tvNamaProfil.text = username

        val menuList = arrayOf(
            "Pengaturan Akun",
            "Privasi & Keamanan",
            "Riwayat Transaksi",
            "Pusat Bantuan",
            "Syarat & Ketentuan",
            "Keluar (Logout)"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            menuList
        )
        binding.lvMenuProfil.adapter = adapter

        binding.lvMenuProfil.setOnItemClickListener { _, _, position, _ ->
            val menuTerpilih = menuList[position]

            if (menuTerpilih == "Keluar (Logout)") {
                tampilkanDialogLogout()
            } else {
                Toast.makeText(requireContext(), "Membuka: $menuTerpilih", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun tampilkanDialogLogout() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin keluar?")
            .setNegativeButton("Tidak") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Ya") { _, _ ->
                val prefs = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
                prefs.edit().clear().apply()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }
            .setCancelable(false)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}