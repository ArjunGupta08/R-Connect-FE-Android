package rconnect.retvens.technologies.dashboard.createRate

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentRatePoliciesBinding


class RatePoliciesFragment : Fragment() {

    lateinit var binding:FragmentRatePoliciesBinding
    private lateinit var robotoMedium : Typeface
    private lateinit var roboto: Typeface
    var count_min = 2
    var count_max = 30


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRatePoliciesBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roboto = ResourcesCompat.getFont(requireContext(),R.font.roboto)!!
        robotoMedium = ResourcesCompat.getFont(requireContext(),R.font.roboto_medium)!!




            var isMon = false
            var isTues = false
            var isWed = false
            var isThur = false
            var frid = false
            var satu = true
            var isSun = false

            binding.fri.setOnClickListener {
                if (!frid){
                    selectCard(binding.fri)
                    frid = true
                }
                else{
                    unSelectCard(binding.fri)
                    frid = false
                }
            }
            binding.saturday.setOnClickListener {
                if (!satu){
                    selectCard(binding.saturday)
                    satu = true
                }
                else{
                    unSelectCard(binding.saturday)
                    satu = false
                }
            }
            binding.sunday.setOnClickListener {
                if (!isSun){
                    selectCard(binding.sunday)
                    isSun = true
                }
                else{
                    unSelectCard(binding.sunday)
                    isSun = false
                }
            }
            binding.monday.setOnClickListener {
                if (!isMon){
                    selectCard(binding.monday)
                    isMon = true
                }
                else{
                    unSelectCard(binding.monday)
                    isMon = false
                }
            }
            binding.tuesday.setOnClickListener {
                if (!isTues){
                    selectCard(binding.tuesday)
                    isTues = true
                }
                else{
                    unSelectCard(binding.tuesday)
                    isTues = false
                }
            }
            binding.wed.setOnClickListener {
                if (!isWed){
                    selectCard(binding.wed)
                    isWed = true
                }
                else{
                    unSelectCard(binding.wed)
                    isWed = false
                }
            }
            binding.thursday.setOnClickListener {
                if (!isThur){
                    selectCard(binding.thursday)
                    isThur = true
                }
                else{
                    unSelectCard(binding.thursday)
                    isThur = false
                }
            }


            var isAllDay = false
            var isweekend = false
            var isWeekDay = false
            var isCustom = false

            fun notClickable(){
                binding.sunday.isClickable = false
                binding.monday.isClickable = false
                binding.tuesday.isClickable = false
                binding.wed.isClickable = false
                binding.thursday.isClickable = false
                binding.fri.isClickable = false
                binding.saturday.isClickable = false
            }
            fun allClickable(){
                binding.sunday.isClickable = true
                binding.monday.isClickable = true
                binding.tuesday.isClickable = true
                binding.wed.isClickable = true
                binding.thursday.isClickable = true
                binding.fri.isClickable = true
                binding.saturday.isClickable = true
            }
            fun selectHead(head: TextView){
                unSelectCard(binding.txtAllDays)
                unSelectCard(binding.txtWeekDays)
                unSelectCard(binding.txtWeekends)
                unSelectCard(binding.txtCustom)

                head.typeface = robotoMedium
                selectCard(head)
            }
            fun unSelectAllDays(){
                unSelectCard(binding.sunday)
                unSelectCard(binding.monday)
                unSelectCard(binding.tuesday)
                unSelectCard(binding.wed)
                unSelectCard(binding.thursday)
                unSelectCard(binding.fri)
                unSelectCard(binding.saturday)
            }

            binding.txtAllDays.setOnClickListener {

                selectHead(binding.txtAllDays)
                selectCard(binding.sunday)
                selectCard(binding.monday)
                selectCard(binding.tuesday)
                selectCard(binding.wed)
                selectCard(binding.thursday)
                selectCard(binding.fri)
                selectCard(binding.saturday)
                notClickable()
                isAllDay = true

            }
            binding.txtWeekends.setOnClickListener {

                selectHead(binding.txtWeekends)
                unSelectAllDays()
                selectCard(binding.saturday)
                selectCard(binding.sunday)
                isweekend = true
                notClickable()

            }
            binding.txtWeekDays.setOnClickListener {

                selectHead(binding.txtWeekDays)
                unSelectAllDays()

                selectCard(binding.monday)
                selectCard(binding.tuesday)
                selectCard(binding.wed)
                selectCard(binding.thursday)
                selectCard(binding.fri)
                isWeekDay = true

            }
            binding.txtCustom.setOnClickListener {

                selectHead(binding.txtCustom)
                unSelectAllDays()
                allClickable()
                isCustom = true
            }


        binding.removeMin.setOnClickListener {
            if (count_min>1)
            count_min--
            binding.countMin.text = "${count_min} Nights"
        }
        binding.addMin.setOnClickListener {
            count_min++
            binding.countMin.text = "${count_min} Nights"
        }

        binding.removeMax.setOnClickListener {
            if (count_max>1){
                count_max--
            }
            binding.countMax.text = "${count_max} Nights"
        }
        binding.addMax.setOnClickListener {
            count_max++
            binding.countMax.text = "${count_max} Nights"
        }

        }

    private fun selectCard(day: TextView?) {
        day?.setBackgroundResource(R.drawable.rounded_black_border)
        day?.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
    }
    private fun unSelectCard(day: TextView?) {
        day?.setBackgroundResource(R.drawable.rounded_white_border)
        day?.setTextColor(ContextCompat.getColor(requireContext(),R.color.lightBlack))
        day?.typeface = roboto
    }


}