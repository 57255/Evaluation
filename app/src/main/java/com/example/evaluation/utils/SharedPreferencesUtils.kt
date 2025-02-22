package com.example.evaluation.utils

import android.content.Context
import com.example.evaluation.logic.Employee
import com.example.evaluation.logic.ExamData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferencesUtils {
    private const val EMPLOYEE_LIST_KEY_Down = "employee_list_down"
    private const val EMPLOYEE_LIST_KEY_Up = "employee_list_up"
    private const val EMPLOYEE_LIST_KEY_Same = "employee_list_same"
    private const val EXAM_DATA_KEY = "exam_list"

    fun saveEmployeeList(context: Context, employeeList: ArrayList<Employee>) {
        val gson = Gson()
        val jsonString = gson.toJson(employeeList)
        val prefs = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString(EMPLOYEE_LIST_KEY_Down, jsonString)
            apply()
        }
    }
    fun saveEmployeeListUp(context: Context, employeeList: ArrayList<Employee>) {
        val gson = Gson()
        val jsonString = gson.toJson(employeeList)
        val prefs = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString(EMPLOYEE_LIST_KEY_Up, jsonString)
            apply()
        }
    }
    fun saveEmployeeListSame(context: Context, employeeList: ArrayList<Employee>) {
        val gson = Gson()
        val jsonString = gson.toJson(employeeList)
        val prefs = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString(EMPLOYEE_LIST_KEY_Same, jsonString)
            apply()
        }
    }
    fun getEmployeeListSame(context: Context): ArrayList<Employee> {
        val prefs = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val jsonString = prefs.getString(EMPLOYEE_LIST_KEY_Same, null)
        return if (jsonString != null) {
            val type = object : TypeToken<List<Employee>>() {}.type
            Gson().fromJson(jsonString, type)
        } else {
            arrayListOf()
        }
    }
    fun getEmployeeList(context: Context): ArrayList<Employee> {
        val prefs = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val jsonString = prefs.getString(EMPLOYEE_LIST_KEY_Down, null)
        return if (jsonString != null) {
            val type = object : TypeToken<List<Employee>>() {}.type
            Gson().fromJson(jsonString, type)
        } else {
            arrayListOf()
        }
    }
    fun getEmployeeListUp(context: Context): ArrayList<Employee> {
        val prefs = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val jsonString = prefs.getString(EMPLOYEE_LIST_KEY_Up, null)
        return if (jsonString != null) {
            val type = object : TypeToken<List<Employee>>() {}.type
            Gson().fromJson(jsonString, type)
        } else {
            arrayListOf()
        }
    }
    fun saveExamList(context: Context,employeeList: ArrayList<ExamData>){
        val gson = Gson()
        val jsonString = gson.toJson(employeeList)
        val prefs = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString(EXAM_DATA_KEY, jsonString)
            apply()
        }
    }
    fun getExamList(context: Context): ArrayList<ExamData> {
        val prefs = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val jsonString = prefs.getString(EXAM_DATA_KEY, null)
        return if (jsonString != null) {
            val type = object : TypeToken<List<ExamData>>() {}.type
            Gson().fromJson(jsonString, type)
        } else {
            arrayListOf()
        }
    }
}
