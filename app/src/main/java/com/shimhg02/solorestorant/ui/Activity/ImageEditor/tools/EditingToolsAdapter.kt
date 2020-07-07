package com.shimhg02.solorestorant.ui.Activity.ImageEditor.tools

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shimhg02.solorestorant.R
import java.util.*


class EditingToolsAdapter(private val mOnItemSelected: OnItemSelected) :
    RecyclerView.Adapter<EditingToolsAdapter.ViewHolder>() {
    private val mToolList: MutableList<ToolModel> =
        ArrayList()

    interface OnItemSelected {
        fun onToolSelected(toolType: ToolType?)
    }

    internal inner class ToolModel(
        val mToolName: String,
        val mToolIcon: Int,
        val mToolType: ToolType
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_editing_tools, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = mToolList[position]
        holder.txtTool.text = item.mToolName
        holder.imgToolIcon.setImageResource(item.mToolIcon)
    }

    override fun getItemCount(): Int {
        return mToolList.size
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var imgToolIcon: ImageView
        var txtTool: TextView

        init {
            imgToolIcon = itemView.findViewById(R.id.imgToolIcon)
            txtTool = itemView.findViewById(R.id.txtTool)
            itemView.setOnClickListener {
                mOnItemSelected.onToolSelected(
                    mToolList[layoutPosition].mToolType
                )
            }
        }
    }

    init {
        mToolList.add(ToolModel("브러쉬", R.drawable.ic_brush, ToolType.BRUSH))
        mToolList.add(ToolModel("텍스트", R.drawable.ic_text, ToolType.TEXT))
        mToolList.add(ToolModel("지우개", R.drawable.ic_eraser, ToolType.ERASER))
        mToolList.add(ToolModel("필터", R.drawable.ic_photo_filter, ToolType.FILTER))
        mToolList.add(ToolModel("이모티콘", R.drawable.ic_insert_emoticon, ToolType.EMOJI))
        mToolList.add(ToolModel("스티커", R.drawable.ic_sticker, ToolType.STICKER))
    }
}