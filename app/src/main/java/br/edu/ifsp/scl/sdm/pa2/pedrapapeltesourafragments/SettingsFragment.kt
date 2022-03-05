package br.edu.ifsp.scl.sdm.pa2.pedrapapeltesourafragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.edu.ifsp.scl.sdm.pa2.pedrapapeltesourafragments.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment() {

    private lateinit var fragmentSettingsViewBinding : FragmentSettingsBinding
    private lateinit var gameSettings: GameSettings
    private lateinit var gameSettingsBackup: GameSettings

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSettingsViewBinding = FragmentSettingsBinding.inflate(inflater, container, false)

        fragmentSettingsViewBinding.saveBt.setOnClickListener{
            gameSettings.isTwoPlayers = fragmentSettingsViewBinding.radio1Rb.isChecked
            gameSettings.round = if(fragmentSettingsViewBinding.round1Rb.isChecked) 1 else if(fragmentSettingsViewBinding.round3Rb.isChecked) 3 else 5
            val editor = sharedPreferences.edit()
            editor.putBoolean("IS_TWO_PLAYERS", gameSettings.isTwoPlayers)
            editor.putInt("NUMBER_OF_ROUNDS", gameSettings.round)
            editor.apply()
            Log.v(
                getString(R.string.app_name),
                "Roger | " + gameSettings.isTwoPlayers.toString()+" | " + gameSettings.round.toString()
            )
            activity?.supportFragmentManager?.popBackStack();
        }

        fragmentSettingsViewBinding.cancelBt.setOnClickListener{
            activity?.supportFragmentManager?.popBackStack();
        }

        gameSettings = GameSettings(true, 1)

        sharedPreferences = activity!!.applicationContext.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        gameSettings.isTwoPlayers = sharedPreferences.getBoolean("IS_TWO_PLAYERS", true)
        gameSettings.round = sharedPreferences.getInt("NUMBER_OF_ROUNDS", 1)

        Log.v(
            getString(R.string.app_name),
            "Roger | " + gameSettings.isTwoPlayers.toString()+" | " + gameSettings.round.toString()
        )

        fragmentSettingsViewBinding.opponentRGroup.check(if(gameSettings.isTwoPlayers) R.id.radio1Rb else R.id.radio2Rb)
        fragmentSettingsViewBinding.roundRGroup.check(if(gameSettings.round == 1) R.id.round1Rb else if(gameSettings.round == 3) R.id.round3Rb else R.id.round5Rb)

        return fragmentSettingsViewBinding.root
    }

}