package com.example.myapplication.adapters
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.Activity

class ActivityAdapter(private val activityList: List<Activity>, private val onItemClick: (Activity) -> Unit) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>()
{
    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val cardActivity: CardView = itemView.findViewById(R.id.cardActivity)
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        val btnCheck: ImageButton = itemView.findViewById(R.id.btnCheck)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activityList[position]
        holder.tvTitulo.text = activity.title

        actualizarEstadoUI(holder, activity.isCompleted)

        holder.btnCheck.setOnClickListener {
            activity.isCompleted = !activity.isCompleted

            actualizarEstadoUI(holder, activity.isCompleted)
        }

        holder.cardActivity.setOnClickListener {
            onItemClick(activity)
        }
    }
    private fun actualizarEstadoUI(holder: ActivityViewHolder, isCompleted: Boolean){
        if(isCompleted) {
            holder.tvEstado.text = "¡Completado!"
            holder.cardActivity.setCardBackgroundColor(Color.parseColor("#C8E6C9"))
            holder.btnCheck.setImageResource(android.R.drawable.checkbox_on_background)
        }else{
            holder.tvEstado.text = "Pendiente de Completar"
            holder.cardActivity.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            holder.btnCheck.setImageResource(android.R.drawable.checkbox_off_background)
        }
    }
    override fun getItemCount(): Int{
        return activityList.size
    }
}