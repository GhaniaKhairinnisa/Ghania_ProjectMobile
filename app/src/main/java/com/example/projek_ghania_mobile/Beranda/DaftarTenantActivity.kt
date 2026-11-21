package com.example.projek_ghania_mobile.Beranda

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projek_ghania_mobile.Data.AppDatabase
import com.example.projek_ghania_mobile.Data.Entity.TenantEntity
import com.example.projek_ghania_mobile.databinding.ActivityDaftarTenantBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DaftarTenantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarTenantBinding
    private lateinit var database: AppDatabase

    private var selectedImageUri: Uri? = null

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                binding.ivFotoTenant.setImageURI(uri)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDaftarTenantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        binding.btnPilihFoto.setOnClickListener {
            imagePicker.launch("image/*")
        }

        binding.btnDaftarTenant.setOnClickListener {
            simpanDataTenant()
        }

    }
    private fun simpanDataTenant() {
        val namaTenant = binding.etNamaTenant.text.toString().trim()
        val namaPemilik = binding.etNamaPemilik.text.toString().trim()
        val kategori = binding.etKategori.text.toString().trim()
        val deskripsi = binding.etDeskripsi.text.toString().trim()
        val nomorTelepon = binding.etNomorTelepon.text.toString().trim()
        val imageUri = selectedImageUri?.toString()

        // Validasi form
        if (namaTenant.isEmpty()) {
            binding.etNamaTenant.error = "Nama tenant harus diisi"
            binding.etNamaTenant.requestFocus()
            return
        }

        if (namaPemilik.isEmpty()) {
            binding.etNamaPemilik.error = "Nama pemilik harus diisi"
            binding.etNamaPemilik.requestFocus()
            return
        }

        if (kategori.isEmpty()) {
            binding.etKategori.error = "Kategori harus diisi"
            binding.etKategori.requestFocus()
            return
        }

        if (deskripsi.isEmpty()) {
            binding.etDeskripsi.error = "Deskripsi harus diisi"
            binding.etDeskripsi.requestFocus()
            return
        }

        if (nomorTelepon.isEmpty()) {
            binding.etNomorTelepon.error = "Nomor telepon harus diisi"
            binding.etNomorTelepon.requestFocus()
            return
        }

        if (imageUri == null) {
            Toast.makeText(
                this,
                "Silakan pilih foto tenant terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Membuat objek tenant
        val tenant = TenantEntity(
            namaTenant = namaTenant,
            namaPemilik = namaPemilik,
            kategori = kategori,
            deskripsi = deskripsi,
            nomorTelepon = nomorTelepon,
            imageUri = imageUri
        )

        // Menyimpan data ke Room
        lifecycleScope.launch {
            try {
                val tenantId = withContext(Dispatchers.IO) {
                    database.tenantDao().insertTenant(tenant)
                }

                Toast.makeText(
                    this@DaftarTenantActivity,
                    "Data tenant berhasil disimpan. ID: $tenantId",
                    Toast.LENGTH_LONG
                ).show()

            } catch (e: Exception) {
                Toast.makeText(
                    this@DaftarTenantActivity,
                    "Gagal menyimpan data tenant",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}