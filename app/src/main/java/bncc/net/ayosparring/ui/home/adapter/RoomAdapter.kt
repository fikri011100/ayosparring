package bncc.net.ayosparring.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bncc.net.ayosparring.databinding.ItemRoomBinding
import bncc.net.ayosparring.model.Room
import bncc.net.ayosparring.model.RoomId
import bncc.net.ayosparring.model.User
import bncc.net.ayosparring.ui.room.DetailRoomActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class RoomAdapter(
    val items: ArrayList<RoomId>,
    val context: Context,
    val category: String,
    val firebase: FirebaseFirestore
) :
    RecyclerView.Adapter<RoomAdapter.ViewHolder>() {

    inner class ViewHolder(val view: ItemRoomBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: RoomId) {
            val data: ArrayList<User> = arrayListOf()
            firebase.collection("user").whereEqualTo("email", item.username).get()
                .addOnSuccessListener { result ->
                    if (result != null) {
                        for (document in result) {
                            data.add(
                                User(
                                    document.data["email"].toString(),
                                    document.data["name"].toString(),
                                    document.data["urlPhoto"].toString()
                                )
                            )
                        }
                        Glide.with(context)
                            .load(data[0].urlPhoto)
                            .into(view.imageCircle)
                    }
                }

            view.textNama.text = item.name
            view.textUsername.text = "@" + item.username
            view.textTitle.text = item.title
            view.textDate.text = item.date + " | " + item.hour
            view.textPlace.text = item.location
            val sisa = item.total - item.joined
            view.textSlot.text = "Slot Tersisa $sisa"

            view.consRoom.setOnClickListener {
                val intent = Intent(context, DetailRoomActivity::class.java)
                intent.putExtra("feature", "Join Room")
                intent.putExtra("id", item.id)
                intent.putExtra("name", item.name)
                intent.putExtra("username", item.username)
                intent.putExtra("title", item.title)
                intent.putExtra("desc", item.desc)
                intent.putExtra("date", item.date)
                intent.putExtra("hour", item.hour)
                intent.putExtra("alamat", item.alamat)
                intent.putExtra("location", item.location)
                intent.putExtra("joined", item.joined)
                intent.putExtra("total", item.total)
                intent.putExtra("category", item.category)
                intent.putExtra("price", item.price)
                intent.putExtra("numberPhone", item.numberPhone)
                intent.putExtra("direction", "home")
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomAdapter.ViewHolder {
        val binding = ItemRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoomAdapter.ViewHolder, position: Int) {
        with(holder) {
            bind(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}