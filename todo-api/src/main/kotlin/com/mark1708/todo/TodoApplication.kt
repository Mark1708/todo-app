package com.mark1708.todo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ToDoApiApplication

fun main(args: Array<String>) {
    runApplication<ToDoApiApplication>(*args)
}