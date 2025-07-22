import java.util.Scanner

fun main() {
    val archives = mutableListOf<Archive>()
    ArchiveMenu(archives).show()
}

data class Archive(val name: String) {
    val notes = mutableListOf<Note>()
}

data class Note(val title: String, val content: String)

abstract class Menu<T>(protected val items: MutableList<T>) {
    protected val scanner = Scanner(System.`in`)
    protected abstract val menuTitle: String
    protected abstract val menuItems: List<String>

    open fun show() {
        while (true) {
            println(menuTitle)
            menuItems.forEachIndexed { index, item -> println("$index. $item") }
            println("${menuItems.size}. Выход")

            val input = scanner.nextLine()

            if (input.isBlank() || !input.all { it.isDigit() }) {
                println("Ошибка: введите номер пункта меню (цифру)")
                continue
            }

            val choice = input.toInt()

            when {
                choice < 0 -> println("Ошибка: номер пункта не может быть отрицательным")
                choice < menuItems.size -> handleChoice(choice)
                choice == menuItems.size -> return
                else -> println("Ошибка: пункта с номером $choice не существует")
            }
        }
    }

    protected abstract fun handleChoice(choice: Int)

    protected fun promptForInput(prompt: String, errorMessage: String): String {
        while (true) {
            println(prompt)
            val input = scanner.nextLine().trim()
            if (input.isNotBlank()) {
                return input
            }
            println(errorMessage)
        }
    }
}

class ArchiveMenu(private val archives: MutableList<Archive>) : Menu<Archive>(archives) {
    override val menuTitle = "Список архивов:"
    override val menuItems = listOf("Создать архив")

    override fun show() {
        while (true) {
            println(menuTitle)

            if (items.isEmpty()) {
                println("Архивов пока нет")
            } else {
                items.forEachIndexed { index, archive -> println("$index. ${archive.name}") }
            }

            menuItems.forEachIndexed { index, item -> println("${items.size + index}. $item") }
            println("${items.size + menuItems.size}. Выход")

            val input = scanner.nextLine()

            if (input.isBlank() || !input.all { it.isDigit() }) {
                println("Ошибка: введите номер пункта меню (цифру)")
                continue
            }

            val choice = input.toInt()

            when {
                choice < 0 -> println("Ошибка: номер пункта не может быть отрицательным")
                choice < items.size -> NoteMenu(items[choice].notes).show()
                choice == items.size -> createArchive()
                choice == items.size + menuItems.size -> return
                else -> println("Ошибка: пункта с номером $choice не существует")
            }
        }
    }

    private fun createArchive() {
        val name = promptForInput(
            "Введите название архива:",
            "Ошибка: название архива не может быть пустым"
        )
        items.add(Archive(name))
        println("Архив '$name' создан")
    }

    override fun handleChoice(choice: Int) {
        NoteMenu(items[choice].notes).show()
    }
}

class NoteMenu(private val notes: MutableList<Note>) : Menu<Note>(notes) {
    override val menuTitle = "Список заметок:"
    override val menuItems = listOf("Создать заметку")

    override fun show() {
        while (true) {
            println(menuTitle)

            if (items.isEmpty()) {
                println("Заметок пока нет")
            } else {
                items.forEachIndexed { index, note -> println("$index. ${note.title}") }
            }

            menuItems.forEachIndexed { index, item -> println("${items.size + index}. $item") }
            println("${items.size + menuItems.size}. Назад")

            val input = scanner.nextLine()

            if (input.isBlank() || !input.all { it.isDigit() }) {
                println("Ошибка: введите номер пункта меню (цифру)")
                continue
            }

            val choice = input.toInt()

            when {
                choice < 0 -> println("Ошибка: номер пункта не может быть отрицательным")
                choice < items.size -> ViewNoteMenu(items[choice]).show()
                choice == items.size -> createNote()
                choice == items.size + menuItems.size -> return
                else -> println("Ошибка: пункта с номером $choice не существует")
            }
        }
    }

    private fun createNote() {
        val title = promptForInput(
            "Введите название заметки:",
            "Ошибка: название заметки не может быть пустым"
        )

        val content = promptForInput(
            "Введите содержание заметки:",
            "Ошибка: содержание заметки не может быть пустым"
        )

        items.add(Note(title, content))
        println("Заметка '$title' создана")
    }

    override fun handleChoice(choice: Int) {
        ViewNoteMenu(items[choice]).show()
    }
}

class ViewNoteMenu(private val note: Note) : Menu<Note>(mutableListOf(note)) {
    override val menuTitle = "Заметка: ${note.title}"
    override val menuItems = emptyList<String>()

    override fun show() {
        println(menuTitle)
        println("\n${note.content}\n")
        println("0. Назад")

        while (true) {
            val input = scanner.nextLine()
            if (input == "0") return
            println("Ошибка: введите 0 для возврата")
        }
    }

    override fun handleChoice(choice: Int) {

    }
}