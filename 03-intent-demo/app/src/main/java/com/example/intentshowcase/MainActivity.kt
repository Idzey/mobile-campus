package com.example.intentshowcase

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.intentshowcase.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        binding.buttonExplicit.setOnClickListener { openExplicit() }
        binding.buttonImplicit.setOnClickListener { openImplicit() }
    }

    /** Текст из поля или null, если поле пустое (тогда показываем ошибку). */
    private fun readText(): String? {
        val text = binding.editMessage.text?.toString()?.trim().orEmpty()
        binding.inputLayout.error = if (text.isEmpty()) getString(R.string.error_empty) else null
        return text.ifEmpty { null }
    }

    // Явный Intent: указываем класс активити напрямую.
    private fun openExplicit() {
        val text = readText() ?: return
        val intent = Intent(this, ResultActivity::class.java)
            .putExtra(ResultActivity.EXTRA_MESSAGE, text)
        startActivity(intent)
    }

    // Неявный Intent: указываем только action, активити ищет система по intent-filter в манифесте.
    private fun openImplicit() {
        val text = readText() ?: return
        val intent = Intent(ResultActivity.ACTION_SHOW_MESSAGE)
            .addCategory(Intent.CATEGORY_DEFAULT)
            .putExtra(ResultActivity.EXTRA_MESSAGE, text)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Snackbar.make(binding.root, R.string.error_no_activity, Snackbar.LENGTH_LONG).show()
        }
    }
}
