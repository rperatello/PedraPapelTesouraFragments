package br.edu.ifsp.scl.sdm.pa2.pedrapapeltesourafragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.util.Log

import br.edu.ifsp.scl.sdm.pa2.pedrapapeltesourafragments.databinding.FragmentMainBinding
import android.widget.Toast
import kotlin.random.Random


class MainFragment : Fragment() {

    private lateinit var fragmentMainViewBinding : FragmentMainBinding

    val EXTRA_CONFIGURACOES = "EXTRA_SETTINGS"

    val IS_TWO_PLAYERS = "IS_TWO_PLAYERS"
    val NUMBER_OF_ROUNDS = "NUMBER_OF_ROUNDS"
    val POINTS_OPPONENT_1 = "POINTS_OPPONENT_1"
    val POINTS_OPPONENT_2 = "POINTS_OPPONENT_2"
    val POINTS_OPPONENT_3 = "POINTS_OPPONENT_3"
    val ROUNDS_FINISHED = "ROUNDS_FINISHED"

    lateinit var sharedPreferences: SharedPreferences

    private var p1 = 0
    private var p2 = 0
    private var p3 = 0
    private var roundsFinished = 0
    private var choiceOp1: Int = 0
    private val hands = arrayOf<Int>(R.mipmap.pedra, R.mipmap.papel, R.mipmap.tesoura)
    private var endGame = false


    private var gameSettings = GameSettings(true, 1)
    private var gameSettingsBackup = GameSettings(true, 1)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // val fragmentMainViewBinding = FragmentMainBinding.inflate(inflater, container, false)
        fragmentMainViewBinding = FragmentMainBinding.inflate(inflater, container, false)

        sharedPreferences = activity!!.applicationContext.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        gameSettings.isTwoPlayers = sharedPreferences.getBoolean(IS_TWO_PLAYERS, true)
        gameSettings.round = sharedPreferences.getInt(NUMBER_OF_ROUNDS, 1)

        fragmentMainViewBinding.btStart.setOnClickListener{
            startGame();
            Log.v(getString(R.string.app_name), "onClick - passou startGame");
        }

        fragmentMainViewBinding.btStone.setOnClickListener{
            choiceOp1 = 0;
            fragmentMainViewBinding.btStone.setBackgroundColor(Color.GRAY);
            fragmentMainViewBinding.opponent1.setImageResource(hands[choiceOp1]);
            play()
        }

        fragmentMainViewBinding.btPaper.setOnClickListener{
            choiceOp1 = 1;
            fragmentMainViewBinding.btPaper.setBackgroundColor(Color.GRAY);
            fragmentMainViewBinding.opponent1.setImageResource(hands[choiceOp1]);
            play()
        }

        fragmentMainViewBinding.btScissor.setOnClickListener{
            choiceOp1 = 2;
            fragmentMainViewBinding.btScissor.setBackgroundColor(Color.GRAY);
            fragmentMainViewBinding.opponent1.setImageResource(hands[choiceOp1]);
            play()
        }

        if (gameSettings.isTwoPlayers)
                fragmentMainViewBinding.imagesSection2.setVisibility(View.GONE)

        return fragmentMainViewBinding.root
    }

    fun play(){
        if (gameSettings.isTwoPlayers) {
            twoPlayers();
        } else {
            threePlayers();
        }
    }

    fun twoPlayers() {
        val random = Random(System.currentTimeMillis())
        val choiceOp2: Int = random.nextInt(3)
        fragmentMainViewBinding.opponent2.setImageResource(hands[choiceOp2])
        resultFor2(choiceOp1, choiceOp2)
        return
    }


    fun threePlayers() {
        val random = Random(System.currentTimeMillis())
        val choiceOp2: Int = random.nextInt(3)
        val choiceOp3: Int? = if (!gameSettings.isTwoPlayers) random.nextInt(3) else null
        fragmentMainViewBinding.opponent2.setImageResource(hands[choiceOp2])
        fragmentMainViewBinding.opponent3.setImageResource(hands[choiceOp3!!])
        fragmentMainViewBinding.imagesSection2.setVisibility(View.VISIBLE)
        resultFor3(choiceOp1, choiceOp2, choiceOp3)
        return
    }

    private fun checkGame() {
//        Log.v(getString(R.string.app_name), "Entrou checkGame: " + gameSettings.toString() + " roundsFinished: " + roundsFinished.toString());
        if (roundsFinished >= gameSettings.round) {
            endGame = true
        }
        return
    }

    private fun resultFor2(op1: Int, op2: Int) {
        val res: Int
        res = compareResult(op1, op2)
        when (res) {
            1 -> {
                p1 += 1
                roundsFinished += 1
                checkGame()
                if (endGame) SendResult() else {
                    Toast.makeText(this@MainFragment.context, getString(R.string.win), Toast.LENGTH_SHORT)
                        .show()
                    cleanAll()
                }
            }
            -1 -> {
                p2 += 1
                roundsFinished += 1
                checkGame()
                if (endGame) SendResult() else {
                    Toast.makeText(this@MainFragment.context, getString(R.string.lost), Toast.LENGTH_SHORT)
                        .show()
                    cleanAll()
                }
            }
            else -> {
                roundsFinished += 1
                checkGame()
                if (endGame) SendResult() else {
                    Toast.makeText(this@MainFragment.context, getString(R.string.toTie), Toast.LENGTH_SHORT)
                        .show()
                    cleanAll()
                }
            }
        }
    }

    private fun resultFor3(op1: Int, op2: Int, op3: Int) {
        val res1 = compareResult(op1, op2)
        val res2 = compareResult(op1, op3)
        val res3 = compareResult(op2, op3)
        if (op1 !== op2 && op1 !== op3 && op2 !== op2) {
            roundsFinished += 1
            checkGame()
            if (endGame) SendResult() else {
                Toast.makeText(this@MainFragment.context, "Vocês empataram!", Toast.LENGTH_SHORT).show()
                cleanAll()
            }
        } else if (res1 == 0 && res3 == 0) {
            roundsFinished += 1
            checkGame()
            if (endGame) SendResult() else {
                Toast.makeText(this@MainFragment.context, "Vocês empataram!", Toast.LENGTH_SHORT).show()
                cleanAll()
            }
        } else if (res1 == 1 && res3 == 0) {
            p1 += 1
            roundsFinished += 1
            checkGame()
            if (endGame) SendResult() else {
                Toast.makeText(this@MainFragment.context, "Você ganhou!", Toast.LENGTH_SHORT).show()
                cleanAll()
            }
        } else if (res1 == 0 && res3 == -1) {
            p3 += 1
            roundsFinished += 1
            checkGame()
            if (endGame) SendResult() else {
                Toast.makeText(this@MainFragment.context, "Oponente 2 ganhou!", Toast.LENGTH_SHORT).show()
                cleanAll()
            }
        } else if (res1 == 1 && res2 == 1) {
            roundsFinished += 1
            checkGame()
            if (endGame) SendResult() else {
                Toast.makeText(this@MainFragment.context, "Vocês empataram!", Toast.LENGTH_SHORT).show()
                cleanAll()
            }
        } else if (res1 == 0 && res2 == 1) {
            p1 += 1
            p2 += 1
            roundsFinished += 1
            checkGame()
            if (endGame) SendResult() else {
                Toast.makeText(
                    this@MainFragment.context,
                    "Você e o Oponente 1 ganharam a jogada!",
                    Toast.LENGTH_SHORT
                ).show()
                cleanAll()
            }
        } else if (res1 == -1 && res3 == 1) {
            p2 += 1
            roundsFinished += 1
            checkGame()
            if (endGame) SendResult() else {
                Toast.makeText(this@MainFragment.context, "Oponente 1 ganhou a jogada!", Toast.LENGTH_SHORT)
                    .show()
                cleanAll()
            }
        } else if (res1 == -1 && res3 == 0) {
            p2 += 1
            p3 += 1
            roundsFinished += 1
            checkGame()
            if (endGame) SendResult() else {
                Toast.makeText(
                    this@MainFragment.context,
                    "Oponente 1 e Oponente 2 ganharam a jogada!",
                    Toast.LENGTH_SHORT
                ).show()
                cleanAll()
            }
        } else if (res1 == 1 && res2 == 0) {
            p1 += 1
            p3 += 1
            roundsFinished += 1
            checkGame()
            if (endGame) SendResult() else {
                Toast.makeText(
                    this@MainFragment.context,
                    "Você e o Oponente 3 ganharam a jogada!",
                    Toast.LENGTH_SHORT
                ).show()
                cleanAll()
            }
        } else {
            roundsFinished += 1
            checkGame()
            if (endGame) SendResult() else {
                Toast.makeText(this@MainFragment.context, "Vocês emparatam!", Toast.LENGTH_SHORT).show()
                cleanAll()
            }
        }
    }

    private fun compareResult(i1: Int, i2: Int): Int {
        return if (i1 === i2) {
            0
        } else if (i1 - i2 == -2 || i1 - i2 == 1) {
            1
        } else {
            -1
        }
    }

    private fun ckWinner(): String? {
//        Log.v(getString(R.string.app_name), "Entrou ckWinner");
        if (p1 === p2 && p1 === p3) {
            return "Os jogadores empataram!"
        } else if (p1 > p2 && p1 > p3) {
            return "Você venceu o jogo!"
        } else if (p1 > p2 && p1 === p3) {
            return "Você e o Oponente 2 empataram!"
        } else if (p1 > p2 && p1 < p3) {
            return "Oponente 2 venceu o jogo!"
        } else if (p1 === p2 && p1 > p3) {
            return "Você e o Oponente 1 empataram!"
        } else if (p1 < p2 && p2 > p3) {
            return "Oponente 1 venceu o jogo!"
        } else if (p1 < p2 && p2 === p3) {
            return "Oponente 1 e Oponente 2 empataram!"
        } else if (p1 < p2 && p2 < p3) {
            return "Oponente 2 venceu o jogo!"
        }
        return "Vocês empataram!"
    }

    private fun SendResult() {
        Log.v(getString(R.string.app_name), "Entrou SendResult")
        resetGame()
        fragmentMainViewBinding.resultSection.setText(ckWinner())
        fragmentMainViewBinding.resultSection.setVisibility(View.VISIBLE)
    }

    fun cleanAll() {
        Log.v(getString(R.string.app_name), "Entrou cleanAll")
        fragmentMainViewBinding.btStone.setBackgroundColor(Color.WHITE)
        fragmentMainViewBinding.btPaper.setBackgroundColor(Color.WHITE)
        fragmentMainViewBinding.btScissor.setBackgroundColor(Color.WHITE)
        return
    }

    private fun startGame() {
        Log.v(getString(R.string.app_name), "Entrou startGame")
        fragmentMainViewBinding.resultSection.setVisibility(View.GONE)
        fragmentMainViewBinding.btStart.setVisibility(View.GONE)
        fragmentMainViewBinding.lbBtSection.setVisibility(View.VISIBLE)
        fragmentMainViewBinding.btSection.setVisibility(View.VISIBLE)
        p1 = 0
        p2 = 0
        p3 = 0
        setNewMatch()
        return
    }

    private fun setNewMatch() {
        Log.v(getString(R.string.app_name), "Entrou setNewMatch")
        fragmentMainViewBinding.opponent1.setImageResource(hands[0])
        fragmentMainViewBinding.opponent2.setImageResource(hands[0])
        fragmentMainViewBinding.opponent3.setImageResource(hands[0])
        cleanAll()
        return
    }

    private fun resetGame() {
        if (gameSettings.isTwoPlayers) fragmentMainViewBinding.imagesSection2.setVisibility(View.GONE) else fragmentMainViewBinding.imagesSection2.setVisibility(
            View.VISIBLE
        )
        fragmentMainViewBinding.resultSection.setVisibility(View.GONE)
        fragmentMainViewBinding.lbBtSection.setVisibility(View.GONE)
        fragmentMainViewBinding.btSection.setVisibility(View.GONE)
        fragmentMainViewBinding.btStart.setVisibility(View.VISIBLE)
        roundsFinished = 0
        endGame = false
        return
    }


}