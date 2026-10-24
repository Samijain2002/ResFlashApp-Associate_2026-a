package com.restest.flashcards.data.repository

import com.restest.flashcards.data.model.FlashCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CardsLocalRepository private constructor() {

    private val cards = mutableListOf(
        FlashCard(
            "Android",
            "What is Android?",
            "Android is an open-source operating system developed by Google, primarily for touchscreen devices. It's based on a modified Linux kernel and offers a flexible platform for application development. Android's user interface supports direct manipulation, using touch inputs that correspond to real-world actions. It also provides extensive customization options, robust multitasking, and a secure environment with regular security updates. Its open-source nature encourages innovation, making it a versatile, user-friendly, and popular choice for developers and users alike."
        ),
        FlashCard(
            "iOS",
            "What is iOS?",
            "iOS is a mobile operating system created and developed by Apple Inc. It is exclusively for Apple's hardware, including the iPhone, iPad, and iPod Touch. It is known for its intuitive interface, robust security, and extensive app ecosystem available through the Apple App Store. iOS also integrates seamlessly with other Apple services and devices, providing a consistent and interconnected user experience."
        ),
        FlashCard(
            "Flutter",
            "What is Flutter?",
            "Flutter is an open-source UI software development kit created by Google. It allows developers to build natively compiled applications for mobile, web, and desktop from a single codebase. Flutter uses the Dart programming language and provides its own widgets, making it highly customizable. It's known for its fast development, expressive and flexible UI, and great performance."
        ),
        FlashCard(
            "KMM",
            "What is Kotlin Multiplatform Mobile?",
            "Kotlin Multiplatform Mobile (KMM) is a software development kit that allows developers to use the same business logic code in both iOS and Android applications. It leverages the flexibility of Kotlin and the benefits of native development to write code once and use it across multiple platforms. KMM helps to reduce time and resources, while maintaining the performance, look, and feel of a native app."
        ),
        FlashCard(
            "React Native",
            "What is React Native?",
            "React Native is an open-source mobile application framework created by Facebook. It is used to develop applications for Android, iOS, Web and UWP by enabling developers to use React along with native platform capabilities."
        ),
        FlashCard(
            "Java",
            "What is Java?",
            "Java is a high-level, class-based, object-oriented programming language that is designed to have as few implementation dependencies as possible."
        ),
        FlashCard(
            "Python",
            "What is Python?",
            "Python is an interpreted high-level general-purpose programming language. Its design philosophy emphasizes code readability with its use of significant indentation."
        ),
        FlashCard(
            "JavaScript",
            "What is JavaScript?",
            "JavaScript, often abbreviated as JS, is a programming language that conforms to the ECMAScript specification. JavaScript is high-level, often just-in-time compiled, and multi-paradigm."
        ),
        FlashCard(
            "Swift",
            "What is Swift?",
            "Swift is a general-purpose, multi-paradigm, compiled programming language developed by Apple Inc. and the open-source community, first released in 2014."
        ),
        FlashCard(
            "C#",
            "What is C#?",
            "C# is a general-purpose, multi-paradigm programming language encompassing static typing, strong typing, lexically scoped, imperative, declarative, functional, generic, object-oriented, and component-oriented programming disciplines."
        ),
        FlashCard(
            "Ruby",
            "What is Ruby?",
            "Ruby is an interpreted, high-level, general-purpose programming language. It was designed and developed in the mid-1990s by Yukihiro 'Matz' Matsumoto in Japan."
        ),
        FlashCard(
            "Go",
            "What is Go?",
            "Go is a statically typed, compiled programming language designed at Google by Robert Griesemer, Rob Pike, and Ken Thompson."
        ),
        FlashCard(
            "Rust",
            "What is Rust?",
            "Rust is a multi-paradigm programming language designed for performance and safety, especially safe concurrency. Rust is syntactically similar to C++, but can guarantee memory safety by using a borrow checker to validate references."
        )
    )

    private val _events =
        MutableStateFlow<Event>(Event.Idle)
    val events = _events.asStateFlow()

    fun requestCards() {
        _events.value = Event.Success(cards)
    }

    fun addCard(newCard: FlashCard) {
        cards.add(newCard)
        _events.value = Event.CardAdded(newCard)
    }

    companion object {
        val instance: CardsLocalRepository by lazy { CardsLocalRepository() }
    }

    sealed class Event {
        data class Success(val cards: List<FlashCard>) : Event()
        data class CardAdded(val card: FlashCard) : Event()
        data class Error(val message: String) : Event()
        data object Idle : Event()
    }
}