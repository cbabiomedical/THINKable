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
import com.example.thinkableproject.utils.EXTRA_BOARD_SIZE
import com.example.thinkableproject.utils.EXTRA_GAME_NAME
import com.github.jinatonic.confetti.CommonConfetti
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.squareup.picasso.Picasso
import java.util.*

class MainActivityK : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val CREATE_REQUEST_CODE = 248
    }

    private lateinit var clRoot: ConstraintLayout
    private lateinit var rvBoard: RecyclerView
    private lateinit var tvNumMoves: TextView
    private lateinit var tvNumPairs: TextView
    private lateinit var information: ImageView
    private lateinit var dialog: Dialog
    private lateinit var dialogIntervention: Dialog
    private lateinit var lineChart: LineChart

    private lateinit var lineData: LineData
    private lateinit var lineDataSet: LineDataSet
    var lineEntries = ArrayList<Entry>()

    //    private lateinit var lineChart: LineChart
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
    private var boardSize = BoardSize.EASY

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
        mainConstraint = findViewById(R.id.mainConstraint)
//        lineChart = findViewById(R.id.lineChartInterventionGame)
//        lineChart = findViewById(R.id.lineChartInterventionGame)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

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
                openDialog()
                CommonConfetti.rainingConfetti(clRoot, intArrayOf(Color.YELLOW, Color.GREEN, Color.MAGENTA)).oneShot()
                firebaseAnalytics.logEvent("won_game") {
                    param("game_name", gameName ?: "[default]")
                    param("board_size", boardSize.name)
                }
            }
        }
        tvNumMoves.text = "Moves: ${memoryGame.getNumMoves()}"
        adapter.notifyDataSetChanged()
    }

    private fun openDialog() {
        var ok: Button
        var lineChart: LineChart

        var lineData: LineData
        var lineDataSet: LineDataSet
        val lineEntries = ArrayList<Entry>()


        dialogIntervention.setContentView(R.layout.game_intervention_popup);
        dialogIntervention.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        ok = dialogIntervention.findViewById(R.id.ok);

        this@MainActivityK.lineEntries = ArrayList<Entry>()
        lineEntries.add(Entry(2f, 34f))
        lineEntries.add(Entry(4f, 56f))
        lineEntries.add(Entry(6f, 65f))
        lineEntries.add(Entry(8f, 23f))
        Log.d("GRAPH ENTRY", lineEntries.toString())
        lineChart = dialogIntervention.findViewById(R.id.lineChartInterventionGame)
        lineDataSet = LineDataSet(lineEntries, "Memory Progress")
        lineData = LineData(lineDataSet)
        lineChart.data = lineData

        lineDataSet.setColors(*ColorTemplate.JOYFUL_COLORS)
        lineDataSet.setValueTextColor(Color.WHITE)
        lineDataSet.setValueTextSize(10f)

        lineChart.setGridBackgroundColor(Color.TRANSPARENT)
        lineChart.setBorderColor(Color.TRANSPARENT)
        lineChart.setGridBackgroundColor(Color.TRANSPARENT)
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.axisRight.textColor = resources.getColor(R.color.white)
        lineChart.axisLeft.textColor = resources.getColor(R.color.white)
        lineChart.legend.textColor = resources.getColor(R.color.white)
        lineChart.description.textColor = R.color.white
        ok.setOnClickListener(View.OnClickListener {
            dialogIntervention.dismiss()
        })

        dialogIntervention.show()

    }

    private fun getEntries() {

    }
}