package com.coddelord.daycounter;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.coddelord.daycounter.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button selectDateTime;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private TextView dateTimeTxt;
    private Calendar calendar;
    private int year, month, dayOfMonth;
    private int hour, minute;
    private long timeDifferenceInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectDateTime = findViewById(R.id.selectDate);
        dateTimeTxt = findViewById(R.id.dateTxt);

        selectDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                                // Seçilen tarihi ayarla
                                year = selectedYear;
                                month = selectedMonth;
                                dayOfMonth = selectedDay;

                                // Saat seçimi için TimePickerDialog'u göster
                                showTimePicker();
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
    }

    private void showTimePicker() {
        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(MainActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // Seçilen saati ve dakikayı ayarla
                        hour = selectedHour;
                        minute = selectedMinute;

                        // Seçilen tarihi ve saati kullanarak bir Calendar nesnesi oluştur
                        Calendar selectedDateTime = Calendar.getInstance();
                        selectedDateTime.set(Calendar.YEAR, year);
                        selectedDateTime.set(Calendar.MONTH, month);
                        selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        selectedDateTime.set(Calendar.MINUTE, selectedMinute);

                        // Şu anki tarihi ve saati kullanarak bir Calendar nesnesi oluştur
                        Calendar currentDateTime = Calendar.getInstance();

                        // İki zaman arasındaki farkı hesapla
                        timeDifferenceInMillis = selectedDateTime.getTimeInMillis() - currentDateTime.getTimeInMillis();

                        // Geri sayımı başlat
                        startCountdown();
                    }
                }, hour, minute, true); // true ile 24 saat formatını kullanabilirsiniz
        timePickerDialog.show();
    }

    private void startCountdown() {
        new CountDownTimer(timeDifferenceInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                // Geri sayım devam ediyor, kalan süreyi hesapla ve göster
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                long days = secondsRemaining / (24 * 60 * 60);
                secondsRemaining = secondsRemaining % (24 * 60 * 60);
                long hours = secondsRemaining / (60 * 60);
                secondsRemaining = secondsRemaining % (60 * 60);
                long minutes = secondsRemaining / 60;
                long seconds = secondsRemaining % 60;

                String countdownText = String.format(Locale.getDefault(),
                        "Kalan Süre: %02d gün %02d saat %02d dakika %02d saniye", days, hours, minutes, seconds);
                dateTimeTxt.setText(countdownText);
            }

            public void onFinish() {
                // Geri sayım tamamlandı
                dateTimeTxt.setText("Süre doldu!");
            }
        }.start();
    }
}
