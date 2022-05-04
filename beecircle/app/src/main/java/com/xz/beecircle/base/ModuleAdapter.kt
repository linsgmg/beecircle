package com.xz.beecircle.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.lang.ClassCastException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

class VBViewHolder<VB : ViewBinding>(val binding: VB, view: View) : BaseViewHolder(view)

abstract class ModuleAdapter<T, VB : ViewBinding>(data: MutableList<T>? = null) : BaseQuickAdapter<T, VBViewHolder<VB>>(0, data) {
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VBViewHolder<VB> {
        val type = javaClass.genericSuperclass as ParameterizedType
        val cls = type.actualTypeArguments[1] as Class<*>
        try {
            val inflate = cls.getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
            val binding = inflate.invoke(null, LayoutInflater.from(parent.context), parent, false) as VB
            return VBViewHolder(binding, binding.root)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return createBaseViewHolder(parent)
    }

    fun setItemClick(listenerMy: OnMyItemClickListener?) {
        this.listenerMy = listenerMy
    }

    var listenerMy: OnMyItemClickListener? = null

    interface OnMyItemClickListener {
        fun onItemClick(position: Int, view: View)
    }
}
