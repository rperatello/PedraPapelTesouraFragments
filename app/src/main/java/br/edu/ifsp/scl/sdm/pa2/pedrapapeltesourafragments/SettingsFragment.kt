package br.edu.ifsp.scl.sdm.pa2.pedrapapeltesourafragments

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSettingsViewBinding = FragmentSettingsBinding.inflate(inflater, container, false)

        fragmentSettingsViewBinding.saveBt.setOnClickListener{
            activity?.supportFragmentManager?.popBackStack();
        }

        fragmentSettingsViewBinding.cancelBt.setOnClickListener{
            activity?.supportFragmentManager?.popBackStack();
        }

        //TODO("Excluir linha 33 e fazer implementar 34 e 35")
        gameSettings = GameSettings(true, 1)
//        gameSettings = (intent.getSerializableExtra(MainActivity.EXTRA_CONFIGURACOES) ?: GameSettings(true, 1)) as GameSettings
//        gameSettingsBackup = (intent.getSerializableExtra(MainActivity.EXTRA_CONFIGURACOES) ?: GameSettings(true, 1)) as GameSettings

        Log.v(
            getString(R.string.app_name),
            "Roger | " + gameSettings.isTwoPlayers.toString()+" | " + gameSettings.round.toString()
        )

        fragmentSettingsViewBinding.opponentRGroup.check(if(gameSettings.isTwoPlayers) R.id.radio1Rb else R.id.radio2Rb)
        fragmentSettingsViewBinding.roundRGroup.check(if(gameSettings.round == 1) R.id.round1Rb else if(gameSettings.round == 3) R.id.round3Rb else R.id.round5Rb)

        return fragmentSettingsViewBinding.root
    }
}