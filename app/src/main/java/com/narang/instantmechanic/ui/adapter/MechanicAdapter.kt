package com.narang.instantmechanic.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.narang.instantmechanic.domain.Mechanic
import com.narang.instantmechanic.databinding.ItemMechanicBinding

class MechanicAdapter(
    private val onMechanicClicked: (Mechanic) -> Unit,
    private val onRequestClicked: (Mechanic) -> Unit,
) : ListAdapter<Mechanic, MechanicAdapter.MechanicViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MechanicViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MechanicViewHolder(
            ItemMechanicBinding.inflate(inflater, parent, false),
            onMechanicClicked,
            onRequestClicked
        )
    }

    override fun onBindViewHolder(holder: MechanicViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MechanicViewHolder(
        private val binding: ItemMechanicBinding,
        private val onMechanicClicked: (Mechanic) -> Unit,
        private val onRequestClicked: (Mechanic) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(mechanic: Mechanic) {
            binding.mechanicName.text = mechanic.name
            binding.mechanicRating.text = mechanic.rating
            binding.mechanicLocation.text = mechanic.location

            binding.root.setOnClickListener {
                onMechanicClicked(mechanic)
            }
            binding.btnBook.setOnClickListener {
                onRequestClicked(mechanic)
            }
            binding.btnSend.setOnClickListener {
                onRequestClicked(mechanic)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Mechanic>() {
            override fun areItemsTheSame(old: Mechanic, new: Mechanic): Boolean =
                old.id == new.id

            override fun areContentsTheSame(old: Mechanic, new: Mechanic): Boolean =
                old == new
        }
    }
}
