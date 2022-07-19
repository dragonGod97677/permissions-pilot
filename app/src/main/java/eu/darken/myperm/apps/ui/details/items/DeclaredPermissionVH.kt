package eu.darken.myperm.apps.ui.details.items

import android.content.res.ColorStateList
import android.view.ViewGroup
import androidx.core.view.isGone
import eu.darken.myperm.R
import eu.darken.myperm.apps.core.features.UsesPermission
import eu.darken.myperm.apps.core.features.UsesPermission.Status
import eu.darken.myperm.apps.ui.details.AppDetailsAdapter
import eu.darken.myperm.common.getColorForAttr
import eu.darken.myperm.common.lists.BindableVH
import eu.darken.myperm.databinding.AppsDetailsPermissionDeclaredItemBinding
import eu.darken.myperm.permissions.core.types.DeclaredPermission

class DeclaredPermissionVH(parent: ViewGroup) :
    AppDetailsAdapter.BaseVH<DeclaredPermissionVH.Item, AppsDetailsPermissionDeclaredItemBinding>(
        R.layout.apps_details_permission_declared_item,
        parent
    ), BindableVH<DeclaredPermissionVH.Item, AppsDetailsPermissionDeclaredItemBinding> {

    override val viewBinding = lazy { AppsDetailsPermissionDeclaredItemBinding.bind(itemView) }

    override val onBindData: AppsDetailsPermissionDeclaredItemBinding.(
        item: Item,
        payloads: List<Any>
    ) -> Unit = { item, _ ->
        val permission = item.permission

        identifier.text = permission.id.value
        label.apply {
            text = permission.getLabel(context)
            isGone = text.isNullOrEmpty()
        }

        statusIcon.apply {
            val (iconRes, tintRes) = when (item.appPermission.status) {
                Status.GRANTED -> R.drawable.ic_baseline_check_circle_24 to R.attr.colorPrimary
                Status.GRANTED_IN_USE -> R.drawable.ic_baseline_check_circle_24 to R.attr.colorPrimary
                Status.DENIED -> R.drawable.ic_baseline_remove_circle_24 to R.attr.colorOnBackground
                Status.UNKNOWN -> R.drawable.ic_baseline_question_mark_24 to R.attr.colorOnBackground
            }
            setImageResource(iconRes)
            imageTintList = ColorStateList.valueOf(context.getColorForAttr(tintRes))
        }

        root.setOnClickListener { item.onItemClicked(item) }
        root.setOnLongClickListener { item.onItemLongClick(item).let { true } }
    }

    data class Item(
        val appPermission: UsesPermission,
        val permission: DeclaredPermission,
        val onItemClicked: (Item) -> Unit,
        val onItemLongClick: (Item) -> Unit,
    ) : AppDetailsAdapter.Item {
        override val stableId: Long
            get() = permission.id.hashCode().toLong()
    }
}