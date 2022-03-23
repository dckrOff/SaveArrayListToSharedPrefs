package com.a1tech.savetochachearraylist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // создание переменных для наших компонентов пользовательского интерфейса.
    private EditText courseNameEdt, courseDescEdt;
    private Button addBtn, saveBtn;
    private RecyclerView courseRV;

    // переменная для нашего класса адаптера и списка массивов
    private CourseAdapter adapter;
    private ArrayList<CourseModal> courseModalArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // инициализируем наши переменные.
        courseNameEdt = findViewById(R.id.idEdtCourseName);
        courseDescEdt = findViewById(R.id.idEdtCourseDescription);
        addBtn = findViewById(R.id.idBtnAdd);
        saveBtn = findViewById(R.id.idBtnSave);
        courseRV = findViewById(R.id.idRVCourses);

        // вызывающий метод для загрузки данных из общих настроек.
        loadData();

        // метод вызова для сборки вид ресайклера.
        buildRecyclerView();

        // прослушиватель кликов для добавления данных в список массивов.
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // строка ниже используется для добавления данных в список массивов.
                courseModalArrayList.add(new CourseModal(courseNameEdt.getText().toString(), courseDescEdt.getText().toString()));
                // уведомление адаптера при добавлении новых данных.
                adapter.notifyItemInserted(courseModalArrayList.size());
            }
        });
        // прослушиватель кликов для сохранения данных в общих настройках.
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // метод вызова для сохранения данных в общих файлах настроек.
                saveData();
            }
        });
    }

    private void buildRecyclerView() {
        // инициализируем наш класс адаптера.
        adapter = new CourseAdapter(courseModalArrayList, MainActivity.this);

        // добавление менеджера компоновки в наше представление ресайклера.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        courseRV.setHasFixedSize(true);

        // настройка менеджера компоновки для нашего представления ресайклера.
        courseRV.setLayoutManager(manager);

        // настройка адаптера для нашего вида ресайклера.
        courseRV.setAdapter(adapter);
    }

    private void loadData() {
        // метод загрузки массива из общих настроек
        // инициализируя наши общие настройки с именем как
        // общие предпочтения.
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        // создание переменной для gson.
        Gson gson = new Gson();

        // ниже строки, чтобы добраться до строки, представленной из нашего
        // общие настройки, если они отсутствуют, устанавливая их как нулевые.
        String json = sharedPreferences.getString("courses", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<CourseModal>>() {
        }.getType();

        // в строке ниже мы получаем данные от gson
        // и сохраняем его в наш список массивов
        courseModalArrayList = gson.fromJson(json, type);

        // проверка ниже, если список массивов пуст или нет
        if (courseModalArrayList == null) {
            // если список массивов пуст
            // создание нового списка массивов.
            courseModalArrayList = new ArrayList<>();
        }
    }

    private void saveData() {
        // способ сохранения данных в списке массивов.
        // создание переменной для хранения данных в общие предпочтения.
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        // создание переменной для редактора хранить данные в общих настройках.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // создание новой переменной для gson.
        Gson gson = new Gson();

        // получение данных от gson и сохранение их в строке.
        String json = gson.toJson(courseModalArrayList);

        Log.e("json", json);

        // нижняя строка предназначена для сохранения данных в общем prefs в виде строки.
        editor.putString("courses", json);

        // нижняя строка для применения изменений и сохранить данные в общих файлах настроек.
        editor.apply();

        // после сохранения данных мы показываем всплывающее сообщение.
        Toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }
}