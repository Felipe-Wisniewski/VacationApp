package br.com.felipewisniewski.vacationplanner.ui.seasons

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.felipewisniewski.vacationplanner.R
import br.com.felipewisniewski.vacationplanner.extensions.MonthNumberToWord
import br.com.felipewisniewski.vacationplanner.model.entity.Selected
import kotlinx.android.synthetic.main.seasons_list.view.*

/**
 * A simple adapter implementation that shows a list of results.
 */
class SeasonsAdapter : RecyclerView.Adapter<SeasonsAdapter.VH>(){

    private val listChoices = arrayListOf<Selected>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.seasons_list, parent, false)
        return VH(layout)
    }

    override fun getItemCount() = listChoices.size

    override fun onBindViewHolder(holder: SeasonsAdapter.VH, position: Int) {
        val from = listChoices[position].dateFrom
        val to = listChoices[position].dateTo
        holder.itemView.tv_month_from.text = from.dropLast(2).MonthNumberToWord()
        holder.itemView.tv_day_from.text = from.drop(2)
        holder.itemView.tv_month_to.text = to.dropLast(2).MonthNumberToWord()
        holder.itemView.tv_day_to.text = to.drop(2)
    }

    class VH(view: View) : RecyclerView.ViewHolder(view)

    /**
     * Receives a list of results and notifies the adapter change.
     */
    fun add(seasonsSelected: ArrayList<Selected>) {
        val max = seasonsSelected.maxBy { it.count }
        listChoices.addAll(seasonsSelected.filter { it.count == max?.count })

        notifyItemInserted(listChoices.lastIndex)
    }
}