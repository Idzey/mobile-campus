package com.example.coffeescreen

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.example.coffeescreen.databinding.ActivityMainBinding
import com.example.coffeescreen.databinding.ItemAddonBinding
import com.google.android.material.snackbar.Snackbar

private data class SizeOption(val label: String, val ml: Int, val kcal: Int, val price: Int)

private data class Addon(@param:StringRes val name: Int, val price: Int)

private val SIZES = listOf(
    SizeOption("S", ml = 250, kcal = 90, price = 190),
    SizeOption("M", ml = 350, kcal = 120, price = 230),
    SizeOption("L", ml = 450, kcal = 160, price = 270),
)

private val SIZE_BUTTONS = listOf(R.id.buttonSizeS, R.id.buttonSizeM, R.id.buttonSizeL)

private val MILK_PRICES = mapOf(
    R.id.chipMilkRegular to 0,
    R.id.chipMilkOat to 50,
    R.id.chipMilkAlmond to 60,
    R.id.chipMilkCoconut to 60,
    R.id.chipMilkNone to 0,
)

private val ADDONS = listOf(
    Addon(R.string.addon_syrup, 40),
    Addon(R.string.addon_shot, 60),
    Addon(R.string.addon_cream, 30),
)

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var sizeIndex = 1
    private var milkId = R.id.chipMilkRegular
    private var sugar = 1
    private val addonChecked = BooleanArray(ADDONS.size)
    private var quantity = 1
    private var favorite = false
    private var descriptionExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        restoreState(savedInstanceState)
        setupInsets()
        setupViews()
        render()
    }

    private fun restoreState(state: Bundle?) {
        state ?: return
        sizeIndex = state.getInt(KEY_SIZE, sizeIndex)
        milkId = state.getInt(KEY_MILK, milkId)
        sugar = state.getInt(KEY_SUGAR, sugar)
        state.getBooleanArray(KEY_ADDONS)?.copyInto(addonChecked)
        quantity = state.getInt(KEY_QUANTITY, quantity)
        favorite = state.getBoolean(KEY_FAVORITE, favorite)
        descriptionExpanded = state.getBoolean(KEY_EXPANDED, descriptionExpanded)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SIZE, sizeIndex)
        outState.putInt(KEY_MILK, milkId)
        outState.putInt(KEY_SUGAR, sugar)
        outState.putBooleanArray(KEY_ADDONS, addonChecked)
        outState.putInt(KEY_QUANTITY, quantity)
        outState.putBoolean(KEY_FAVORITE, favorite)
        outState.putBoolean(KEY_EXPANDED, descriptionExpanded)
    }

    private fun setupInsets() {
        val barPadding = resources.getDimensionPixelSize(R.dimen.bar_padding)
        val buttonMargin = resources.getDimensionPixelSize(R.dimen.hero_button_margin)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { root, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            // Hero уходит под статус-бар, поэтому кнопки опускаем на его высоту.
            root.updatePadding(left = bars.left, right = bars.right)
            binding.buttonBack.updateLayoutParams<ViewGroup.MarginLayoutParams> { topMargin = bars.top + buttonMargin }
            binding.buttonFavorite.updateLayoutParams<ViewGroup.MarginLayoutParams> { topMargin = bars.top + buttonMargin }
            // Нижняя панель стоит над навигационной панелью.
            binding.bottomBar.barContent.updatePadding(bottom = barPadding + bars.bottom)
            insets
        }
    }

    private fun setupViews() {
        val details = binding.details
        val bar = binding.bottomBar

        binding.buttonBack.setOnClickListener { finish() }
        binding.buttonFavorite.setOnClickListener {
            favorite = !favorite
            render()
        }

        details.toggleSize.check(SIZE_BUTTONS[sizeIndex])
        details.toggleSize.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                sizeIndex = SIZE_BUTTONS.indexOf(checkedId)
                render()
            }
        }

        details.groupMilk.check(milkId)
        details.groupMilk.setOnCheckedStateChangeListener { _, checkedIds ->
            checkedIds.firstOrNull()?.let {
                milkId = it
                render()
            }
        }

        details.sliderSugar.value = sugar.toFloat()
        details.sliderSugar.addOnChangeListener { _, value, _ ->
            sugar = value.toInt()
            render()
        }

        details.textMore.setOnClickListener {
            descriptionExpanded = !descriptionExpanded
            render()
        }

        ADDONS.forEachIndexed { index, addon ->
            val row = ItemAddonBinding.inflate(layoutInflater, details.containerAddons, false)
            row.checkAddon.setText(addon.name)
            row.checkAddon.isChecked = addonChecked[index]
            row.checkAddon.setOnCheckedChangeListener { _, isChecked ->
                addonChecked[index] = isChecked
                render()
            }
            row.textAddonPrice.text = getString(R.string.price_plus_format, addon.price)
            row.textAddonPrice.setOnClickListener { row.checkAddon.toggle() }
            details.containerAddons.addView(row.root)
        }

        bar.buttonMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                render()
            }
        }
        bar.buttonPlus.setOnClickListener {
            quantity++
            render()
        }
        bar.buttonAddToCart.setOnClickListener {
            val message = getString(R.string.added_format, quantity, SIZES[sizeIndex].label, total())
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                .setAnchorView(bar.root)
                .show()
        }
    }

    private fun total(): Int {
        val addons = ADDONS.indices.filter { addonChecked[it] }.sumOf { ADDONS[it].price }
        val unit = SIZES[sizeIndex].price + MILK_PRICES.getValue(milkId) + addons
        return unit * quantity
    }

    private fun render() {
        val details = binding.details
        val bar = binding.bottomBar
        val size = SIZES[sizeIndex]

        details.textSizeValue.text = getString(R.string.volume_format, size.ml)
        details.chipVolume.text = getString(R.string.volume_format, size.ml)
        details.chipKcal.text = getString(R.string.kcal_format, size.kcal)

        details.textSugar.text =
            if (sugar == 0) getString(R.string.sugar_none)
            else resources.getQuantityString(R.plurals.sugar_spoons, sugar, sugar)

        details.textDescription.maxLines = if (descriptionExpanded) Int.MAX_VALUE else DESCRIPTION_COLLAPSED_LINES
        details.textMore.setText(if (descriptionExpanded) R.string.less else R.string.more)

        bar.textQty.text = quantity.toString()
        bar.textTotal.text = getString(R.string.price_format, total())
        bar.buttonMinus.isEnabled = quantity > 1

        binding.buttonFavorite.setIconResource(
            if (favorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        )
        binding.buttonFavorite.iconTint = ContextCompat.getColorStateList(
            this, if (favorite) R.color.heart else R.color.coffee_dark
        )
    }

    private companion object {
        const val DESCRIPTION_COLLAPSED_LINES = 3
        const val KEY_SIZE = "size"
        const val KEY_MILK = "milk"
        const val KEY_SUGAR = "sugar"
        const val KEY_ADDONS = "addons"
        const val KEY_QUANTITY = "quantity"
        const val KEY_FAVORITE = "favorite"
        const val KEY_EXPANDED = "expanded"
    }
}
