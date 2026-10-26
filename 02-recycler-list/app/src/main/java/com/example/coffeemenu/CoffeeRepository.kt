package com.example.coffeemenu

object CoffeeRepository {

    val items: List<CoffeeItem> = listOf(
        CoffeeItem(1, "Эспрессо", "Классический крепкий шот, 30 мл", 150, R.drawable.ic_cup),
        CoffeeItem(2, "Американо", "Эспрессо с горячей водой, 250 мл", 180, R.drawable.ic_cup),
        CoffeeItem(3, "Капучино", "Эспрессо, молоко и плотная пенка, 250 мл", 230, R.drawable.ic_cup),
        CoffeeItem(4, "Латте", "Много молока и нежная пенка, 350 мл", 250, R.drawable.ic_cup),
        CoffeeItem(5, "Флэт уайт", "Двойной эспрессо с бархатным молоком, 200 мл", 260, R.drawable.ic_cup),
        CoffeeItem(6, "Раф", "Сливки, ванильный сахар и эспрессо, 300 мл", 290, R.drawable.ic_cup),
        CoffeeItem(7, "Мокко", "Эспрессо, шоколад и молоко, 300 мл", 280, R.drawable.ic_cup),
        CoffeeItem(8, "Айс латте", "Холодный латте со льдом, 400 мл", 270, R.drawable.ic_iced),
        CoffeeItem(9, "Эспрессо тоник", "Эспрессо, тоник и лёд, 300 мл", 280, R.drawable.ic_iced),
        CoffeeItem(10, "Холодный брю", "Кофе холодной экстракции, 350 мл", 300, R.drawable.ic_iced),
        CoffeeItem(11, "Глясе", "Эспрессо с шариком пломбира, 250 мл", 270, R.drawable.ic_iced),
        CoffeeItem(12, "Какао", "Горячий шоколадный напиток, 300 мл", 220, R.drawable.ic_cup),
        CoffeeItem(13, "Чай с бергамотом", "Чёрный чай, лимон и мёд, 400 мл", 200, R.drawable.ic_cup),
        CoffeeItem(14, "Матча латте", "Японский зелёный чай с молоком, 350 мл", 310, R.drawable.ic_cup),
        CoffeeItem(15, "Лимонад", "Домашний лимонад с мятой, 400 мл", 240, R.drawable.ic_iced),
    )
}
