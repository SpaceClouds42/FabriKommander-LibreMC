package me.gserv.fabrikommander.extension

import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.ItemStack

fun ItemStack.applyGlint() {
    this.orCreateTag.putBoolean("Glint", true)
    this.addEnchantment(null, 0)
}

fun ItemStack.removeGlint() {
    this.removeSubTag("Glint")
    val enchantments = EnchantmentHelper.get(this.copy())
    this.removeSubTag("Enchantments")
    for (e in enchantments) {
        this.addEnchantment(e.key, e.value)
    }
}