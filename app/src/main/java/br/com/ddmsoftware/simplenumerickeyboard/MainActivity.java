package br.com.ddmsoftware.simplenumerickeyboard;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.System.exit;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = new String ("br.com.ddmsoftware.simplenumerickeyboard.MESSAGE");

    MediaPlayer errorSound;
    MediaPlayer okSound;

    Character[] aSignal = {'+', '-', '/', 'x'};
    Boolean bZeroDivision = false;

    float fPercAcertos = 0;
    int iCountAcertos = 0;
    int iCountErros = 0;

    boolean STOP_COUNTDOWN = false;
    TextView tvNumber1;
    TextView tvSignal;
    TextView tvNumber2;
    TextView tvResultCalculado;
    TextView tvResultDigitado;
    TextView tvCountDown;

    TextView tvGridResult_TotAcertos;
    TextView tvGridResult_TotErros;

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.tvResultDigitado);
        tvNumber1 = (TextView)findViewById(R.id.tvFirstNumber);
        tvNumber2 = (TextView)findViewById(R.id.tvSecondNumber);
        tvSignal = (TextView) findViewById(R.id.tvSignal);
        tvResultDigitado = (TextView) findViewById(R.id.tvResultDigitado);
        tvResultCalculado = (TextView) findViewById(R.id.tvResultCalculado);

        tvGridResult_TotAcertos = (TextView) findViewById(R.id.tvGridResult_TotalAcertos);
        tvGridResult_TotErros = (TextView) findViewById(R.id.tvGridResult_TotalErros);

        tvCountDown = (TextView)findViewById(R.id.tvCountdown);

        final Button btnOne = (Button) findViewById(R.id.btnOne);
        final Button btnTwo = (Button) findViewById(R.id.btnTwo);
        final Button btnThree = (Button) findViewById(R.id.btnThree);
        final Button btnFour = (Button) findViewById(R.id.btnFour);
        final Button btnFive = (Button) findViewById(R.id.btnFive);
        final Button btnSix = (Button) findViewById(R.id.btnSix);
        final Button btnSeven = (Button) findViewById(R.id.btnSeven);
        final Button btnEight = (Button) findViewById(R.id.btnEight);
        final Button btnNine = (Button) findViewById(R.id.btnNine);
        final Button btnZero = (Button) findViewById(R.id.btnZero);
        final Button btnSend = (Button)findViewById(R.id.btnSend);
        final Button btnErase = (Button) findViewById(R.id.btnErase);

        tvGridResult_TotErros.setText("");
        tvGridResult_TotAcertos.setText("");
        tvCountDown.setText("");
        textView.setText("");

        errorSound = MediaPlayer.create(MainActivity.this, R.raw.error);
        okSound = MediaPlayer.create(MainActivity.this,R.raw.ok);

        btnErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tvResultDigitado.getText().length()>0)
                    tvResultDigitado.setText(tvResultDigitado.getText().toString().substring(0,tvResultDigitado.getText().length()-1));
            }
        });

        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getKeyboardEntry(btnOne);
            }
        });
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getKeyboardEntry(btnTwo);
            }
        });

        btnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getKeyboardEntry(btnThree);
            }
        });

        btnFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getKeyboardEntry(btnFour);
            }
        });

        btnFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getKeyboardEntry(btnFive);
            }
        });

        btnSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getKeyboardEntry(btnSix);
            }
        });

        btnSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getKeyboardEntry(btnSeven);
            }
        });

        btnEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getKeyboardEntry(btnEight);
            }
        });

        btnNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getKeyboardEntry(btnNine);
            }
        });

        btnZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getKeyboardEntry(btnZero);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tvResultDigitado.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "DIGITE UM VALOR ANTES DE CONTINUAR.", Toast.LENGTH_SHORT).show();
                }
                else {

                    int iValorDigitado = Integer.parseInt(tvResultDigitado.getText().toString());
                    int iValorCalculado = Integer.parseInt(tvResultCalculado.getText().toString());

                    if (validaResultado(iValorCalculado, iValorDigitado)) {
                        iCountAcertos++;

                        Toast.makeText(getBaseContext(), "Resposta Correta", Toast.LENGTH_SHORT).show();
                    } else {
                        iCountErros++;
                        Toast.makeText(getBaseContext(), "Resposta INCORRETA... BURRO!", Toast.LENGTH_SHORT).show();

                    }

                    fPercAcertos = iCountAcertos*100/(iCountAcertos+iCountErros);

                    tvGridResult_TotAcertos.setText(String.valueOf(iCountAcertos));
                    tvGridResult_TotErros.setText(String.valueOf(iCountErros));

                    clearFields(true);
                    generateCalc();
                }
            }
        });

        // Enable Notification Service (12 hours)
        NotificationEventReceiver.setupAlarm(getApplicationContext());

        STOP_COUNTDOWN = false;
        mCountdown("60");

        //clearFields(true);
        generateCalc();
    }

    @Override
    protected void onResume() {
        super.onResume();

        errorSound = MediaPlayer.create(MainActivity.this, R.raw.error);
        okSound = MediaPlayer.create(MainActivity.this,R.raw.ok);

        iCountAcertos = 0;
        iCountErros = 0;
        fPercAcertos = 0;

        tvGridResult_TotAcertos.setText("");
        tvGridResult_TotErros.setText("");

        generateCalc();

        STOP_COUNTDOWN = false;
        mCountdown("60");
    }

    private void getKeyboardEntry(Button mButtonText) {
        textView.setText(textView.getText()+ mButtonText.getText().toString());
    }

    private void sendNotification(){

        Toast.makeText(MainActivity.this, "Passei aqui", Toast.LENGTH_SHORT).show();

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("My Notification")
                .setContentText("Hello World")
                .setSmallIcon(R.mipmap.ic_launcher);

        NotificationCompat.InboxStyle inboxStyle = new android.support.v4.app.NotificationCompat.InboxStyle();

        String[] events = new String[6];

        inboxStyle.setBigContentTitle("Event tracker details: ");

        for (int i=0; i < events.length; i++) {

            inboxStyle.addLine(events[i]);
        }

        mBuilder.setStyle(inboxStyle);
    }

    private void sendNotification2() {

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello World! " + dateFormat.format(date));
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, ResultActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ResultActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        //mNotificationManager.notify(mId, mBuilder.build());
        mNotificationManager.notify(1, mBuilder.build());
    }


    private void mCountdown(final String pSeconds) {
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            int i = Integer.parseInt(pSeconds);

            public void run() {

                System.out.println(i--);
                if ( (i< 0) || (STOP_COUNTDOWN ==true)) {
                    timer.cancel();
                }

                // Apenas para atualizar o User Interface
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCountDown.setText("Countdown:\n" + (i+1));

                        if ((STOP_COUNTDOWN==true) || i<0) {
                            Toast.makeText(MainActivity.this, "Total de Acertos: " + iCountAcertos + "\n + Total de Erros: " + iCountErros, Toast.LENGTH_SHORT).show();

                            String strResultado = "Time is up\n================" +
                                    "\nTotal de Acertos: " + iCountAcertos +
                                    "\nTotal de Erros: " + iCountErros ;

                            String sResultado = iCountAcertos +";" + iCountErros + ";" + fPercAcertos;

                            Toast.makeText(getApplicationContext(), sResultado, Toast.LENGTH_LONG).show();

                            // Exibe resultado em outra Activity
                            Intent intent = new Intent(getApplication(), ResultActivity.class);
                            intent.putExtra(EXTRA_MESSAGE, sResultado);

                            startActivity(intent);
                        }

                    }
                });
            }
        }, 0, 1000);
    }
    
    public void clearFields(boolean pClear) {
        /*

        if (pClear==true) {
            tvNumber1.setVisibility(View.INVISIBLE);
            tvSignal.setVisibility(View.INVISIBLE);
            tvNumber2.setVisibility(View.INVISIBLE);
        }
        */
    }


    private boolean validaResultado(int pValorCalculado, int pValorDigitado) {

        boolean ok;
        if (pValorCalculado == pValorDigitado) {
            ok = true;
            //okSound.start();
        }
        else {
            ok = false;
            //zerrorSound.start();
        }
        return ok;
    }



    private void generateCalc() {

        tvResultDigitado.setText("");

        Random random = new Random();

        float iFirstNumber = random.nextInt(40);
        float iSecondNumber = random.nextInt(10);
        int iSignal = random.nextInt(4);
        String sSignal = aSignal[iSignal].toString();
        //String sSignal = "/";//aSignal[iSignal].toString();

        bZeroDivision = false;
        int resto = 0;
        float Result = 0;

        clearFields(false);

        if (sSignal.equals("+"))
            Result = iFirstNumber + iSecondNumber;
        else if (sSignal.equals("-")) {
            Result = iFirstNumber - iSecondNumber;
            if (Result <0) generateCalc();
            bZeroDivision = true;
        }
        else {
            if (sSignal.equals("/")) {

                resto = (int) (iFirstNumber % iSecondNumber);

                if (resto != 0 || iFirstNumber == 0.0 || iSecondNumber == 0.0) {
                    generateCalc();
                    bZeroDivision = true;
                } else
                    Result = iFirstNumber / iSecondNumber;
            } else if (sSignal.equals("x")) {
                iFirstNumber = random.nextInt(10);
                Result = iFirstNumber * iSecondNumber;
            }
        }

        if (bZeroDivision == false) {

            String sFirstNumber = String.format("%.0f", iFirstNumber);
            String sSecondNumber = String.format("%.0f", iSecondNumber);
            //String sResult = String.valueOf(Result);
            //if (!sSignal.equals("/"))
            String sResult = String.format("%.0f", Result);

            tvNumber1.setText(sFirstNumber);
            tvSignal.setText(sSignal.toString());
            tvNumber2.setText(sSecondNumber);
            tvResultCalculado.setText(sResult);
        }
    }

}
