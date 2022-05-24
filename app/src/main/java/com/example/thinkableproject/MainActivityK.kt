package com.example.thinkableproject

import android.animation.ArgbEvaluator
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thinkableproject.creation.CreatActivity
import com.example.thinkableproject.models.BoardSize
import com.example.thinkableproject.models.MemoryGame
import com.example.thinkableproject.models.UserImageList
import com.example.thinkableproject.sample.JsonPlaceHolder
import com.example.thinkableproject.utils.EXTRA_BOARD_SIZE
import com.example.thinkableproject.utils.EXTRA_GAME_NAME
import com.github.jinatonic.confetti.CommonConfetti
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivityK : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val CREATE_REQUEST_CODE = 248
        var isStarted: Boolean = false;

    }

    private lateinit var clRoot: ConstraintLayout
    private lateinit var rvBoard: RecyclerView
    private lateinit var tvNumMoves: TextView
    private lateinit var tvNumPairs: TextView
    private lateinit var information: ImageView
    private lateinit var dialog: Dialog
    private lateinit var dialogIntervention: Dialog
    private var mInterstitialAd: InterstitialAd? = null
    lateinit var mUser:FirebaseUser
    lateinit var jsonPlaceHolder:JsonPlaceHolder
    var startTime: Long = 0
    var endTime: Long = 0
    var x:Int = 0
    var c:Int = 0
    var sum=0.0;
    var average=0.0;

    //    private lateinit var lineChart: LineChart
    private var points: Int = 0;

    private lateinit var lineData: LineData
    private lateinit var lineDataSet: LineDataSet
    var lineEntries = ArrayList<Entry>()

    private lateinit var lineChart: LineChart
    var color: Int = 0
    lateinit var mainConstraint: ConstraintLayout
    lateinit var gameVideo: VideoView
    private val db = Firebase.firestore
    private val firebaseAnalytics = Firebase.analytics
    private val remoteConfig = Firebase.remoteConfig
    private var gameName: String? = null
    private var customGameImages: List<String>? = null
    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var database: FirebaseFirestore
    private var boardSize = BoardSize.EASY
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        clRoot = findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)
        information = findViewById(R.id.gameInfo);
        dialog = Dialog(this);
        dialogIntervention = Dialog(this)
        gameVideo = findViewById(R.id.simpleVideo);
        database = FirebaseFirestore.getInstance()
        mainConstraint = findViewById(R.id.mainConstraint)
        startTime = System.currentTimeMillis();
        Log.d("StartTime", startTime.toString())
        isStarted = true
//        val xVal: ArrayList<Float?> = ArrayList<Any?>()
//        val yVal = ArrayList<Float>()
        mUser = FirebaseAuth.getInstance().currentUser!!
//        lineChart = findViewById(R.id.lineChartInterventionGame)
//        lineChart = findViewById(R.id.lineChartInterventionGame)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        MobileAds.initialize(this) { createPersonalizedAd() }


        var adView = AdView(this)

        adView.adSize = AdSize.BANNER

        adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"


        MobileAds.initialize(this) { }

        adView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)


        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }

        val adSize = AdSize(300, 50)


//    remoteConfig.setDefaultsAsync(mapOf("about_link" to "https://www.youtube.com/rpandey1234", "scaled_height" to 250L, "compress_quality" to 60L))
//    remoteConfig.fetchAndActivate()
//      .addOnCompleteListener(this) { task ->
//        if (task.isSuccessful) {
//          Log.i(TAG, "Fetch/activate succeeded, did config get updated? ${task.result}")
//        } else {
//          Log.w(TAG, "Remote config fetch failed")
//        }
//      }
        setupBoard()
        val prefsCardIn = getSharedPreferences("prefsCardIn", MODE_PRIVATE)
        val firstStartCardIn = prefsCardIn.getBoolean("firstStartCardIn", true)

        if (firstStartCardIn) {
            displayPopUp()
        }
        information.setOnClickListener {
            displayPopUp();
        }
    }

    private fun createPersonalizedAd() {
        val adRequest = AdRequest.Builder().build()
        createInstestialAd(adRequest)
    }

    private fun createInstestialAd(adRequest: AdRequest) {
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/8691691433", adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd
                        Log.i("TAG", "onAdLoaded")
                        mInterstitialAd!!.setFullScreenContentCallback(object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d("TAG", "The ad was dismissed.")
                                dialogIntervention.dismiss()


//                                startActivity(Intent(applicationContext, MainActivityK::class.java))
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show.")
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null
                                Log.d("TAG", "The ad was shown.")
                            }
                        })
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Handle the error
                        Log.i("TAG", loadAdError.message)
                        mInterstitialAd = null
                    }
                })

    }

    private fun displayPopUp() {
        var ok: Button
        var c1: View
        var c2: View

        dialog.setContentView(R.layout.cardgame_popup)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        ok = dialog.findViewById(R.id.ok);
//        c1=dialog.findViewById(R.id.c1);
//        c2=dialog.findViewById(R.id.c2);
        var mUser: FirebaseUser

        mUser = FirebaseAuth.getInstance().currentUser!!

//        val c = Calendar.getInstance()
//        val timeOfDay = c[Calendar.HOUR_OF_DAY]
//
//        val colorreference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.uid).child("theme")
//        colorreference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                Log.d("FirebaseColor PopUp Kotlin", snapshot.value.toString())
//                color = (snapshot.getValue() as Long).toInt()
//                Log.d("Color", color.toString())
//                if (color == 2) {  //light theme
//                    c1.visibility = View.INVISIBLE //c1 ---> dark blue , c2 ---> light blue
//                    c2.visibility = View.VISIBLE
//                } else if (color == 1) { //light theme
//                    c1.visibility = View.VISIBLE
//                    c2.visibility = View.INVISIBLE
//                } else {
//                    if (timeOfDay >= 0 && timeOfDay < 12) { //light theme
//                        c1.visibility = View.INVISIBLE
//                        c2.visibility = View.VISIBLE
//                    } else if (timeOfDay >= 12 && timeOfDay < 16) { //dark theme
//                        c1.visibility = View.INVISIBLE
//                        c2.visibility = View.VISIBLE
//                    } else if (timeOfDay >= 16 && timeOfDay < 24) { //dark theme
//                        c1.visibility = View.VISIBLE
//                        c2.visibility = View.INVISIBLE
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {}
//        })

        ok.setOnClickListener {
            dialog.dismiss()
            gameVideo.visibility = View.VISIBLE
            mainConstraint.visibility = View.GONE
            gameVideo.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.cardgame))
            gameVideo.start()

            gameVideo.setOnCompletionListener {
                gameVideo.visibility = View.GONE
                mainConstraint.visibility = View.VISIBLE
            }
        }

        dialog.show()
        val prefsCardIn = getSharedPreferences("prefsCardIn", MODE_PRIVATE)
        val editor = prefsCardIn.edit()
        editor.putBoolean("firstStartCardIn", false)
        editor.apply()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_refresh -> {
                isStarted = true
                startTime = System.currentTimeMillis()
                if (memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()) {

                    showAlertDialog("Quit your current game?", null, View.OnClickListener {
                        setupBoard()
                    })
                } else {
                    setupBoard()
                }
                return true
            }
            R.id.mi_new_size -> {
                isStarted = true
                startTime = System.currentTimeMillis()
                showNewSizeDialog()
                return true
            }
//      R.id.mi_custom -> {
//        showCreationDialog()
//        return true
//      }
//      R.id.mi_download -> {
//        showDownloadDialog()
//        return true
//      }
//      R.id.mi_about -> {
//        firebaseAnalytics.logEvent("open_about_link", null)
//        val aboutLink = remoteConfig.getString("about_link")
//        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(aboutLink)))
//        return true
//      }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val customGameName = data?.getStringExtra(EXTRA_GAME_NAME)
            if (customGameName == null) {
                Log.e(TAG, "Got null custom game from CreateActivity")
                return
            }
            downloadGame(customGameName)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showDownloadDialog() {
        val boardDownloadView = LayoutInflater.from(this).inflate(R.layout.dialog_download_board, null)
        showAlertDialog("Fetch memory game", boardDownloadView, View.OnClickListener {
            val etDownloadGame = boardDownloadView.findViewById<EditText>(R.id.etDownloadGame)
            val gameToDownload = etDownloadGame.text.toString().trim()
            downloadGame(gameToDownload)
        })
    }

    private fun downloadGame(customGameName: String) {
        if (customGameName.isBlank()) {
            Snackbar.make(clRoot, "Game name can't be blank", Snackbar.LENGTH_LONG).show()
            Log.e(TAG, "Trying to retrieve an empty game name")
            return
        }
        firebaseAnalytics.logEvent("download_game_attempt") {
            param("game_name", customGameName)
        }
        db.collection("games").document(customGameName).get().addOnSuccessListener { document ->
            val userImageList = document.toObject(UserImageList::class.java)
            if (userImageList?.images == null) {
                Log.e(TAG, "Invalid custom game data from Firebase")
                Snackbar.make(clRoot, "Sorry, we couldn't find any such game, '$customGameName'", Snackbar.LENGTH_LONG).show()
                return@addOnSuccessListener
            }
            firebaseAnalytics.logEvent("download_game_success") {
                param("game_name", customGameName)
            }
            val numCards = userImageList.images.size * 2
            boardSize = BoardSize.getByValue(numCards)
            customGameImages = userImageList.images
            gameName = customGameName
            // Pre-fetch the images for faster loading
            for (imageUrl in userImageList.images) {
                Picasso.get().load(imageUrl).fetch()
            }
            Snackbar.make(clRoot, "You're now playing '$customGameName'!", Snackbar.LENGTH_LONG).show()
            setupBoard()
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Exception when retrieving game", exception)
        }
    }

    private fun showCreationDialog() {
        firebaseAnalytics.logEvent("creation_show_dialog", null)
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radioGroupSize)
        showAlertDialog("Create your own memory board", boardSizeView, View.OnClickListener {
            val desiredBoardSize = when (radioGroupSize.checkedRadioButtonId) {
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMedium -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            firebaseAnalytics.logEvent("creation_start_activity") {
                param("board_size", desiredBoardSize.name)
            }
            val intent = Intent(this, CreatActivity::class.java)
            intent.putExtra(EXTRA_BOARD_SIZE, desiredBoardSize)
            startActivityForResult(intent, CREATE_REQUEST_CODE)
        })
    }

    private fun showNewSizeDialog() {
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radioGroupSize)
        when (boardSize) {
            BoardSize.EASY -> radioGroupSize.check(R.id.rbEasy)
            BoardSize.MEDIUM -> radioGroupSize.check(R.id.rbMedium)
            BoardSize.HARD -> radioGroupSize.check(R.id.rbHard)
        }
        showAlertDialog("Choose new size", boardSizeView, View.OnClickListener {
            boardSize = when (radioGroupSize.checkedRadioButtonId) {
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMedium -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            gameName = null
            customGameImages = null
            setupBoard()
        })
    }

    private fun showAlertDialog(title: String, view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
                .setTitle(title)
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK") { _, _ ->
                    positiveClickListener.onClick(null)
                    if (mInterstitialAd != null) {
                        mInterstitialAd!!.show(this@MainActivityK)
                    } else {
                        positiveClickListener.onClick(null)
                    }

                }.show()
    }

    private fun setupBoard() {
        supportActionBar?.title = gameName ?: getString(R.string.app_name)
        memoryGame = MemoryGame(boardSize, customGameImages)
        when (boardSize) {
            BoardSize.EASY -> {
                tvNumMoves.text = "Easy: 4 x 2"
                tvNumPairs.text = "Pairs: 0/4"
            }
            BoardSize.MEDIUM -> {
                tvNumMoves.text = "Medium: 6 x 3"
                tvNumPairs.text = "Pairs: 0/9"
            }
            BoardSize.HARD -> {
                tvNumMoves.text = "Hard: 6 x 4"
                tvNumPairs.text = "Pairs: 0/12"
            }
        }
        tvNumPairs.setTextColor(ContextCompat.getColor(this, R.color.color_progress_none))
        adapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards, object : MemoryBoardAdapter.CardClickListener {
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }
        })
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())

    }

    private fun updateGameWithFlip(position: Int) {
        // Error handling:
        if (memoryGame.haveWonGame()) {
            Snackbar.make(clRoot, "You already won! Use the menu to play again.", Snackbar.LENGTH_LONG).show()
            return
        }
        if (memoryGame.isCardFaceUp(position)) {
            Snackbar.make(clRoot, "Invalid move!", Snackbar.LENGTH_SHORT).show()
            return
        }

        // Actually flip the card
        if (memoryGame.flipCard(position)) {
            Log.i(TAG, "Found a match! Num pairs found: ${memoryGame.numPairsFound}")
            val color = ArgbEvaluator().evaluate(
                    memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(),
                    ContextCompat.getColor(this, R.color.color_progress_none),
                    ContextCompat.getColor(this, R.color.color_progress_full)
            ) as Int
            tvNumPairs.setTextColor(color)
            tvNumPairs.text = "Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
            if (memoryGame.haveWonGame()) {
                Snackbar.make(clRoot, "You won! Congratulations.", Snackbar.LENGTH_LONG).show()
                Log.d("Memory Move", memoryGame.getNumMoves().toString())
                Log.d("BoardSize", boardSize.numCards.toString())
                if (boardSize.numCards == 8) {
                    if (memoryGame.getNumMoves() < 6) {
                        points = 50;
                    } else if (5 < memoryGame.getNumMoves() && memoryGame.getNumMoves() < 10) {
                        points = 25;
                    } else {
                        points = 5;
                    }

                } else if (boardSize.getNumPairs() == 18) {
                    if (memoryGame.getNumMoves() < 11) {
                        points = 50;
                    } else if (11 < memoryGame.getNumMoves() && memoryGame.getNumMoves() < 16) {
                        points = 30;
                    } else if (16 < memoryGame.getNumMoves() && memoryGame.getNumMoves() < 21) {
                        points = 15;
                    } else {
                        points = 5;
                    }
                } else {
                    if (memoryGame.getNumMoves() < 16) {
                        points = 50;
                    } else if (16 < memoryGame.getNumMoves() && memoryGame.getNumMoves() < 21) {
                        points = 30;
                    } else if (21 < memoryGame.getNumMoves() && memoryGame.getNumMoves() < 26) {
                        points = 15;
                    } else {
                        points = 5;
                    }
                }
                Log.d("Final Points", points.toString())
                database.collection("users")
                        .document(FirebaseAuth.getInstance().uid!!)
                        .get().addOnSuccessListener { documentSnapshot ->
                            user = documentSnapshot.toObject(User::class.java)!!
                            //                binding.currentCoins.setText(String.valueOf(user.getCoins()));
                            Log.d("Current Coins", user.coins.toString())
                            Log.d("High Score Inside", points.toString())
                            val updatedCoins = (user.coins + points) as Long
                            Log.d("Updated High Score", updatedCoins.toString())
                            //                binding.currentCoins.setText(user.getCoins() + "");
                            database.collection("users").document(FirebaseAuth.getInstance().uid!!)
                                    .update("coins", updatedCoins).addOnSuccessListener {
//                                        Toast.makeText(this, "Successfully Updated Coins", Toast.LENGTH_SHORT).show()
                                    }.addOnFailureListener { e ->
                                        Log.d("Error", e.toString())
//                                        Toast.makeText(this, "Failed to Update Coins", Toast.LENGTH_SHORT).show()
                                    }
                        }
                isStarted = false
                endTime = System.currentTimeMillis();
                val seconds = (endTime - startTime) / 1000;
                Log.d("endTime", endTime.toString())
                Log.d("SecondsCard", seconds.toString())

                val sh = getSharedPreferences("prefsTimeMemWH", MODE_APPEND)

                val shs = getSharedPreferences("prefsTimeMemWH", MODE_APPEND)
                val now1 = Calendar.getInstance()
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                Log.d("WEEK", now1[Calendar.WEEK_OF_MONTH].toString())
                Log.d("MONTH", now1[Calendar.MONTH].toString())
                Log.d("YEAR", now1[Calendar.YEAR].toString())
                Log.d("DAY", now1[Calendar.DAY_OF_MONTH].toString())

                val month1 = now1[Calendar.MONTH] + 1
                val day = now1[Calendar.DAY_OF_MONTH] + 1
                val f: Format = SimpleDateFormat("EEEE")
                val str1 = f.format(Date())
                x = sh.getInt("firstStartTimeMemWH", 0)


                Log.d("VideoIndex", BroadcastReceiver_BTLE_GATT.memoryCarGame_index.toString())

                val gson = GsonBuilder()
                        .setLenient()
                        .create()
                val client = OkHttpClient.Builder()
                        .connectTimeout(120000, TimeUnit.SECONDS)
                        .readTimeout(120000, TimeUnit.SECONDS).build()

                val retrofit = Retrofit.Builder().baseUrl("http://192.168.8.137:5000/").client(client)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
                jsonPlaceHolder = retrofit.create(JsonPlaceHolder::class.java)

                Log.d("BroadcastVid", BroadcastReceiver_BTLE_GATT.memoryCarGame_index.toString())

                if (BroadcastReceiver_BTLE_GATT.memoryCarGame_index.size > 0) {
                    for (i in BroadcastReceiver_BTLE_GATT.memoryCarGame_index.indices) {
                        Log.d("GETI", BroadcastReceiver_BTLE_GATT.memoryCarGame_index.toString())
                        sum += BroadcastReceiver_BTLE_GATT.memoryCarGame_index[i] as Double
                    }
                    Log.d("SUMRELS", sum.toString())
                    average = sum / BroadcastReceiver_BTLE_GATT.memoryCarGame_index.size
                    Log.d("SUMRELS", average.toString())
                    val averageD = java.lang.Double.valueOf(String.format("%.3g%n", average))
                    val referenceIntervention = FirebaseDatabase.getInstance().getReference("Users").child(mUser.uid).child("CardGameIntervention").child(now1[Calendar.YEAR].toString()).child(month1.toString()).child(now1[Calendar.WEEK_OF_MONTH].toString()).child(str1).child(x.toString())
                    referenceIntervention.setValue(averageD)

                    val referenceIntervention1 = FirebaseDatabase.getInstance().getReference("MemoryIndex").child(mUser.uid).child(now1.get(Calendar.YEAR).toString()).child(month1.toString()).child(now1.get(Calendar.WEEK_OF_MONTH).toString()).child(str1).child(x.toString())
                    referenceIntervention1.setValue(averageD)
                }
                if (BroadcastReceiver_BTLE_GATT.memoryCarGame_index.size == 0) {
                    average = 0.0
                }

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show


// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show


// We can then use the data

// We can then use the data
                Log.d("A Count", x.toString())

                val y: Int = x + 1

                val prefsCount1 = getSharedPreferences("prefsTimeMemWH", MODE_PRIVATE)
                val editor = prefsCount1.edit()
                editor.putInt("firstStartTimeMemWH", y)
                editor.apply()
                val sha = getSharedPreferences("prefsTimeMemWH", MODE_APPEND)

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show


// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show
                val x1 = sha.getInt("firstStartTimeMemWH", 0)

                Log.d("A Count2", x1.toString())
                val now= Calendar.getInstance()
                val simpleDateFormat1 = SimpleDateFormat("dd/MM/yyyy")
                Log.d("WEEK", now[Calendar.WEEK_OF_MONTH].toString())
                Log.d("MONTH", now[Calendar.MONTH].toString())
                Log.d("YEAR", now[Calendar.YEAR].toString())
                Log.d("DAY", now[Calendar.DAY_OF_MONTH].toString())

                val month = now[Calendar.MONTH] + 1
                val day1 = now[Calendar.DAY_OF_MONTH] + 1
                val f1: Format = SimpleDateFormat("EEEE")
                val str = f1.format(Date())
                mUser= FirebaseAuth.getInstance().currentUser!!
                val reference = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Memory Games").child(now.get(Calendar.YEAR).toString()).child(month.toString()).child(now.get(Calendar.WEEK_OF_MONTH).toString()).child(str).child(x.toString())
                reference.setValue(seconds)


                openDialog()
                CommonConfetti.rainingConfetti(clRoot, intArrayOf(Color.YELLOW, Color.GREEN, Color.MAGENTA)).oneShot()
                firebaseAnalytics.logEvent("won_game") {
                    param("game_name", gameName ?: "[default]")
                    param("board_size", boardSize.name)
                }

            }
        }
        tvNumMoves.text = "Moves: ${memoryGame.getNumMoves()}"
        Log.d("Moves", memoryGame.getNumMoves().toString());
        adapter.notifyDataSetChanged()
    }

    private fun openDialog() {
        var ok: Button
//        var pointsCal: TextView;
//        var totalPoints: TextView
        var lineChart: LineChart


        var lineData: LineData
        var lineDataSet: LineDataSet
        val lineEntries = ArrayList<Entry>()
        val now = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        Log.d("WEEK", now[Calendar.WEEK_OF_MONTH].toString())
        Log.d("MONTH", now[Calendar.MONTH].toString())
        Log.d("YEAR", now[Calendar.YEAR].toString())
        Log.d("DAY", now[Calendar.DAY_OF_MONTH].toString())
        Log.d("Month", now[Calendar.MONTH].toString())

        val month = now[Calendar.MONTH] + 1
        val day = now[Calendar.DAY_OF_MONTH] + 1
        val f: Format = SimpleDateFormat("EEEE")
        val str = f.format(Date())

        dialogIntervention.setContentView(R.layout.game_intervention_popup);
        dialogIntervention.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        ok = dialogIntervention.findViewById(R.id.ok);
        lineChart = dialogIntervention.findViewById(R.id.lineChartInterventionGame)


        this@MainActivityK.lineEntries = ArrayList<Entry>()
        val referenceIntervention = FirebaseDatabase.getInstance().getReference("Users").child(mUser.uid).child("CardGameIntervention").child(now.get(Calendar.YEAR).toString()).child(month.toString()).child(now.get(Calendar.WEEK_OF_MONTH).toString()).child(str)
        referenceIntervention!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    Log.d("XVAL", dataSnapshot!!.key!!)
                    val xxVal = dataSnapshot!!.key!!.toFloat()
//                    Log.d("XArrayList", xVal.toString())
                    val yyVal: Float = dataSnapshot!!.value.toString().toFloat()
                    Log.d("YVAL", yyVal.toString())
//                    Log.d("YArrayList", yVal.toString())
                    lineEntries.add(Entry(xxVal, yyVal))
                    lineDataSet = LineDataSet(lineEntries, "Card Game Progress on $str")
                    lineData = LineData(lineDataSet)
                    lineChart.data = lineData
                    lineDataSet.setColors(*ColorTemplate.JOYFUL_COLORS)
                    lineDataSet.valueTextColor = Color.WHITE
                    lineDataSet.valueTextSize = 10f
                    lineChart.setGridBackgroundColor(Color.TRANSPARENT)
                    lineChart.setBorderColor(Color.TRANSPARENT)
                    lineChart.setGridBackgroundColor(Color.TRANSPARENT)
                    lineChart.axisLeft.setDrawGridLines(false)
                    lineChart.xAxis.setDrawGridLines(false)
                    lineChart.axisRight.setDrawGridLines(false)
                    lineChart.xAxis.textColor = R.color.white
                    lineChart.axisRight.textColor = resources.getColor(R.color.white)
                    lineChart.axisLeft.textColor = resources.getColor(R.color.white)
                    lineChart.legend.textColor = resources.getColor(R.color.white)
                    lineChart.description.textColor = R.color.white
                    lineChart.invalidate()
                    lineChart.refreshDrawableState()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })



        ok.setOnClickListener(View.OnClickListener {
            dialogIntervention.dismiss()
//            if (mInterstitialAd != null) {
//                mInterstitialAd!!.show(this@MainActivityK)
//            } else {
//                val intent = Intent(this@MainActivityK, MainActivityK::class.java)
//                startActivity(intent)
            finish()
//            }
        })

        dialogIntervention.show()

    }


}