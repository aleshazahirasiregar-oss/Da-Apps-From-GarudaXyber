package com.note.styles.util

data class LanguageItem(
    val code: String,
    val flag: String,
    val nativeName: String,
    val englishName: String,
    val tag: String
)

object AppLanguages {
    val list = listOf(
        LanguageItem(
            code = "id",
            flag = "🇮🇩",
            nativeName = "Bahasa Indonesia",
            englishName = "Indonesian",
            tag = "Rekomendasi"
        ),
        LanguageItem(
            code = "en",
            flag = "🇺🇸",
            nativeName = "English",
            englishName = "English",
            tag = "Default"
        ),
        LanguageItem(
            code = "es",
            flag = "🇪🇸",
            nativeName = "Español",
            englishName = "Spanish",
            tag = "Popular"
        ),
        LanguageItem(
            code = "ja",
            flag = "🇯🇵",
            nativeName = "日本語",
            englishName = "Japanese",
            tag = "Popular"
        )
    )

    fun getByCode(code: String): LanguageItem {
        return list.find { it.code == code } ?: list[0]
    }
}

class AppStrings(val languageCode: String) {

    private val isId = languageCode == "id"
    private val isEs = languageCode == "es"
    private val isJa = languageCode == "ja"

    // Onboarding / Setup
    val welcomeBadge: String = when {
        isId -> "Aplikasi Catatan Estetik"
        isEs -> "Bloc de Notas Estético"
        isJa -> "美しいノートアプリ"
        else -> "Aesthetic Notes App"
    }

    val welcomeTitle: String = when {
        isId -> "Selamat Datang di NoteS!"
        isEs -> "¡Bienvenido a NoteS!"
        isJa -> "NoteSへようこそ！"
        else -> "Welcome to NoteS!"
    }

    val welcomeSubtitle: String = when {
        isId -> "Buat catatan indah dengan lebih dari 2.000.000 kombinasi gaya tema visual unik."
        isEs -> "Crea notas hermosas con más de 2,000,000 de combinaciones de estilos temáticos visuales."
        isJa -> "200万通り以上の美しいビジュアルテーマで、あなただけのノートを作成できます。"
        else -> "Create beautiful notes with over 2,000,000 unique visual theme style variations."
    }

    val featuresHeader: String = when {
        isId -> "Fitur Unggulan Aplikasi"
        isEs -> "Características Principales"
        isJa -> "主な機能と魅力"
        else -> "App Highlights & Features"
    }

    // Feature items
    val feat1Title: String = when {
        isId -> "2,000,000+ Tema Visual Prosedural"
        isEs -> "Más de 2,000,000 Temas Visuales"
        isJa -> "200万以上のプロシージャルテーマ"
        else -> "2,000,000+ Procedural Themes"
    }
    val feat1Desc: String = when {
        isId -> "Setiap nomor tema menghasilkan kombinasi warna gradien, kontras teks, dan palet yang unik secara matematis."
        isEs -> "Cada número de tema genera una combinación matemática única de colores degradados y paletas."
        isJa -> "テーマ番号ごとに、独自のグラデーション、背景、コントラストが自動計算されます。"
        else -> "Every theme number mathematically generates unique gradient papers, text contrast, and palettes."
    }

    val feat2Title: String = when {
        isId -> "Kustomisasi Kertas & Bentuk Kartu"
        isEs -> "Personalización de Papel y Formas"
        isJa -> "用紙パターンとカード形状"
        else -> "Paper Patterns & Card Shapes"
    }
    val feat2Desc: String = when {
        isId -> "Pilih pola kertas (garis buku, dot grid, vintage parchment, cyber circuits), bentuk kartu, perangko, dan washi tape."
        isEs -> "Elige patrones de papel (rayado, puntos, pergamino, circuitos), formas de tarjeta, sellos y cinta washi."
        isJa -> "罫線、ドット、ヴィンテージ羊皮紙、サイバー回路、スタンプやマスキングテープを選べます。"
        else -> "Select lined rules, dot grids, vintage parchment, cyber circuits, card shapes, stamps, and washi tape."
    }

    val feat3Title: String = when {
        isId -> "Theme Roulette (Acak Gaya Cepat)"
        isEs -> "Ruleta de Temas Instantánea"
        isJa -> "テーマルーレット (瞬時にランダム)"
        else -> "Instant Theme Roulette"
    }
    val feat3Desc: String = when {
        isId -> "Sentuh tombol dadu untuk mendapatkan inspirasi kombinasi tema visual acak yang memukau dalam sekejap."
        isEs -> "Toca el dado para obtener inspiración instantánea con estilos visuales estéticos al azar."
        isJa -> "サイコロボタンをタップするだけで、おしゃれなランダムテーマを瞬時に楽しめます。"
        else -> "Roll the dice for instant stunning aesthetic style inspiration in just one tap."
    }

    val feat4Title: String = when {
        isId -> "Checklist & To-Do Interaktif"
        isEs -> "Listas de Tareas Interactivas"
        isJa -> "インタラクティブなチェックリスト"
        else -> "Interactive Checklists"
    }
    val feat4Desc: String = when {
        isId -> "Format daftar belanja, kegiatan harian, atau target dengan kotak centang interaktif langsung di kartu catatan."
        isEs -> "Crea listas de tareas pendientes con casillas de verificación directamente en tus notas."
        isJa -> "買い物リストや日課を、チェックボックス付きで簡単に管理できます。"
        else -> "Create to-do lists and check off completed items directly from your styled note cards."
    }

    val feat5Title: String = when {
        isId -> "Theme Vault (Brankas Tema)"
        isEs -> "Bóveda de Temas Favoritos"
        isJa -> "テーマ保管庫 (Vault)"
        else -> "Favorite Theme Vault"
    }
    val feat5Desc: String = when {
        isId -> "Simpan gaya tema yang Anda sukai ke dalam koleksi pribadi untuk dipakai kembali pada catatan berikutnya."
        isEs -> "Guarda tus estilos temáticos favoritos en tu colección personal para reutilizarlos."
        isJa -> "お気に入りのテーマスタイルを保存して、いつでも新しいノートに再利用できます。"
        else -> "Save custom styles you love into your personal vault to reuse anytime."
    }

    val feat6Title: String = when {
        isId -> "100% Offline & Sangat Privat"
        isEs -> "100% Offline y Privado"
        isJa -> "100% オフライン＆安全"
        else -> "100% Offline & Private"
    }
    val feat6Desc: String = when {
        isId -> "Semua data catatan tersimpan di memori perangkat Anda secara aman tanpa memerlukan internet."
        isEs -> "Todos los datos de tus notas se almacenan de forma segura en la memoria de tu dispositivo."
        isJa -> "すべてのノートは端末ローカルに安全に保存され、インターネット接続は不要です。"
        else -> "All your notes stay secure, private, and local on your device with no internet needed."
    }

    val buttonLetsGo: String = "Let's Go 🚀"

    // Language selection screen
    val selectLanguageTitle: String = when {
        isId -> "Pilih Bahasa Aplikasi"
        isEs -> "Seleccionar Idioma"
        isJa -> "言語の選択"
        else -> "Choose Your Language"
    }

    val selectLanguageSubtitle: String = when {
        isId -> "Pilih bahasa tampilan yang Anda inginkan. Anda dapat mengubahnya kapan saja nanti dari menu atas:"
        isEs -> "Selecciona tu idioma preferido. Puedes cambiarlo en cualquier momento desde el menú superior:"
        isJa -> "使用する言語を選択してください。設定は後からいつでも変更できます:"
        else -> "Select your preferred interface language. You can change this anytime from the top bar:"
    }

    val buttonStartApp: String = when {
        isId -> "Mulai Mencatat ✨"
        isEs -> "Comenzar Ahora ✨"
        isJa -> "アプリを始める ✨"
        else -> "Get Started ✨"
    }

    // Main Note List
    val searchPlaceholder: String = when {
        isId -> "Cari catatan, tema, atau kategori..."
        isEs -> "Buscar notas, temas o categorías..."
        isJa -> "ノート、テーマ、カテゴリを検索..."
        else -> "Search notes, themes, or categories..."
    }

    val filterAll: String = when {
        isId -> "Semua"
        isEs -> "Todas"
        isJa -> "すべて"
        else -> "All"
    }

    val filterPinned: String = when {
        isId -> "📌 Disematkan"
        isEs -> "📌 Fijadas"
        isJa -> "📌 ピン留め"
        else -> "📌 Pinned"
    }

    val filterFavorites: String = when {
        isId -> "❤️ Favorit"
        isEs -> "❤️ Favoritas"
        isJa -> "❤️ お気に入り"
        else -> "❤️ Favorites"
    }

    val newNote: String = when {
        isId -> "Catatan Baru"
        isEs -> "Nueva Nota"
        isJa -> "新規ノート"
        else -> "New Note"
    }

    val quickRoulette: String = when {
        isId -> "Tema Acak"
        isEs -> "Ruleta"
        isJa -> "ルーレット"
        else -> "Roulette"
    }

    val vault: String = when {
        isId -> "Brankas Tema"
        isEs -> "Bóveda"
        isJa -> "保管庫"
        else -> "Vault"
    }

    val notesCountSuffix: String = when {
        isId -> "catatan bergaya"
        isEs -> "notas diseñadas"
        isJa -> "件のノート"
        else -> "styled notes"
    }

    val emptyTitle: String = when {
        isId -> "Belum ada catatan"
        isEs -> "No hay notas aún"
        isJa -> "ノートがありません"
        else -> "No notes yet"
    }

    val emptySubtitle: String = when {
        isId -> "Ketuk tombol (+) untuk membuat catatan baru atau coba tombol Dadu untuk tema acak!"
        isEs -> "¡Toca (+) para crear una nueva nota o prueba la Ruleta para un tema aleatorio!"
        isJa -> "(+) ボタンで新しいノートを作成するか、サイコロボタンでランダムテーマを試しましょう！"
        else -> "Tap (+) to create a new note or roll the Theme Roulette for a random style!"
    }

    val languageSettingsTitle: String = when {
        isId -> "Pengaturan Bahasa"
        isEs -> "Configuración de Idioma"
        isJa -> "言語設定"
        else -> "Language Settings"
    }

    val guideNoteButton: String = when {
        isId -> "Buka Panduan Fitur NoteS"
        isEs -> "Abrir Guía de NoteS"
        isJa -> "NoteS機能ガイドを開く"
        else -> "Open NoteS Feature Guide"
    }
}
