package br.com.ddmsoftware.simplenumerickeyboard;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TableGameActivity extends AppCompatActivity {

    TableGame tableGame = new TableGame();

    Timer timer;

    public static final String EXTRA_MESSAGE = new String ("br.com.ddmsoftware.simplenumerickeyboard.MESSAGE");

    Button btnStartNewGame;
    MediaPlayer errorSound;
    MediaPlayer okSound;

    Character[] aSignal = {'+', '-', '/', 'x'};
    Boolean bZeroDivision = false;

    float fPercAcertos = 0;
    int iCountAcertos = 0;
    int iCountErros = 0;

    float iOldFistNumber = 0;
    float iOldSecondNumber = 0;

    boolean STOP_COUNTDOWN = false;
    boolean TO_VAZANDO = false;
    //boolean FIRST_RUN = true;

    LinearLayout linearMainCountainer;
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
        setContentView(R.layout.activity_table_game);

        linearMainCountainer = (LinearLayout) findViewById(R.id.linearMainCountainer);

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
        btnStartNewGame = (Button)findViewById(R.id.btnStartNewGame);

        errorSound = MediaPlayer.create(TableGameActivity.this, R.raw.error);
        okSound = MediaPlayer.create(TableGameActivity.this,R.raw.ok);

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

        linearMainCountainer.setVisibility(View.INVISIBLE);


        btnErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvResultDigitado.getText().length()>0)
                    tvResultDigitado.setText(tvResultDigitado.getText().toString().substring(0,tvResultDigitado.getText().length()-1));
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

                    if (tableGame.validateResult(iValorCalculado, iValorDigitado)) {
                        //okSound.start();
                        iCountAcertos++;

                        Toast.makeText(getBaseContext(), "Resposta Correta", Toast.LENGTH_SHORT).show();
                    } else {
                        //errorSound.start();
                        iCountErros++;
                        Toast.makeText(getBaseContext(), "Resposta INCORRETA... BURRO!", Toast.LENGTH_SHORT).show();

                    }

                    fPercAcertos = iCountAcertos*100/(iCountAcertos+iCountErros);

                    tvGridResult_TotAcertos.setText(String.valueOf(iCountAcertos));
                    tvGridResult_TotErros.setText(String.valueOf(iCountErros));

                    tableGame.generateNewCalc();

                }
            }
        });

        btnStartNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tableGame.startNotificationService();
                tableGame.startNewGame(true);
                tableGame.loadAdvertisement();

                btnStartNewGame.setVisibility(View.INVISIBLE);
                linearMainCountainer.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        Toast.makeText(getApplicationContext(), "Passei no OnDestroy", Toast.LENGTH_SHORT).show();

        tableGame.stopCountDown();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnStartNewGame.setVisibility(View.VISIBLE);
        linearMainCountainer.setVisibility(View.INVISIBLE);

        timer = new Timer();
    }

    private void getKeyboardEntry(Button mButtonText) {
        textView.setText(textView.getText()+ mButtonText.getText().toString());
    }

    public class TableGame {

        private void initializeVariables() {

            errorSound = MediaPlayer.create(TableGameActivity.this, R.raw.error);
            okSound = MediaPlayer.create(TableGameActivity.this,R.raw.ok);

            iCountAcertos = 0;
            iCountErros = 0;
            fPercAcertos = 0;

            tvGridResult_TotAcertos.setText("");
            tvGridResult_TotErros.setText("");
            tvCountDown.setText("");
            textView.setText("");

        }

        private void startCountDown(final String pSeconds){

            //final Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {

                int i = Integer.parseInt(pSeconds);

                public void run() {

                    System.out.println(i--);

                    STOP_COUNTDOWN = false;

                    if ( (i<= 0) || (STOP_COUNTDOWN==true)) {
                        timer.cancel();

                        i = 0 ;
                        STOP_COUNTDOWN = true;

                    }

                    // Apenas para atualizar o User Interface
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //tvCountDown.setText("Countdown:\n" + (i+1));
                            tvCountDown.setText(String.valueOf(i));

                            if (STOP_COUNTDOWN) {

                                //Thread.currentThread().interrupt();

                                Toast.makeText(TableGameActivity.this, "Total de Acertos: " + iCountAcertos + "\n + Total de Erros: " + iCountErros, Toast.LENGTH_SHORT).show();

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

        private void stopCountDown() {
            timer.cancel();
        }

        private void startNewGame(boolean pStartOnResume) {

            if (pStartOnResume) {
                // Inicia as variaveis globais
                initializeVariables();

                // Gera um Novo Calculo
                generateNewCalc();

                // Iniciar a Contagem Regressiva
                startCountDown("61");
            }
        }

        private void generateNewCalcBKP() {

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

            //clearFields(false);

            if (sSignal.equals("+"))
                Result = iFirstNumber + iSecondNumber;
            else if (sSignal.equals("-")) {
                Result = iFirstNumber - iSecondNumber;
                if (Result <0) generateNewCalc();
                bZeroDivision = true;
            }
            else {
                if (sSignal.equals("/")) {

                    resto = (int) (iFirstNumber % iSecondNumber);

                    if (resto != 0 || iFirstNumber == 0.0 || iSecondNumber == 0.0) {
                        generateNewCalc();
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

        private void generateNewCalc() {

            tvResultDigitado.setText("");

            Random random = new Random();

            float iFirstNumber = random.nextInt(40);
            float iSecondNumber = random.nextInt(10);

            if ((iOldFistNumber==iFirstNumber) && (iOldSecondNumber==iSecondNumber)){

                generateNewCalc();

            } else {

                iOldFistNumber = iFirstNumber;
                iOldSecondNumber = iSecondNumber;
            }


            int iSignal = random.nextInt(4);
            String sSignal = aSignal[iSignal].toString();
            //String sSignal = "/";//aSignal[iSignal].toString();

            bZeroDivision = false;
            int resto = 0;
            float Result = 0;

            //clearFields(false);

            if (sSignal.equals("+"))
                Result = iFirstNumber + iSecondNumber;
            else if (sSignal.equals("-")) {
                Result = iFirstNumber - iSecondNumber;
                if (Result <0) generateNewCalc();
                bZeroDivision = true;
            }
            else {
                if (sSignal.equals("/")) {

                    resto = (int) (iFirstNumber % iSecondNumber);

                    if (resto != 0 || iFirstNumber == 0.0 || iSecondNumber == 0.0) {
                        generateNewCalc();
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

        private boolean validateResult(int pCalculatedValue, int pTypedValue) {
            boolean ok;
            if (pCalculatedValue == pTypedValue) {
                ok = true;
                //okSound.start();
            }
            else {
                ok = false;
                //zerrorSound.start();
            }
            return ok;
        }

        private void startNotificationService() {
            // Enable Notification Service (12 hours)
            NotificationEventReceiver.setupAlarm(getApplicationContext());
        }

        private void loadAdvertisement() {

            // Create a AdView
            // Load Advertisement Banner
            AdView mAdView = (AdView) findViewById(R.id.adViewTableGame);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

    }


/*    private void sendNotification(){

        Toast.makeText(TableGameActivity.this, "Passei aqui", Toast.LENGTH_SHORT).show();

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
*/

}
