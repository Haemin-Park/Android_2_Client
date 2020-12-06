package com.example.sport_planet.presentation.board

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sport_planet.R
import com.example.sport_planet.data.enums.MenuEnum
import com.example.sport_planet.data.enums.SeparatorEnum
import com.example.sport_planet.data.model.MenuModel
import com.example.sport_planet.data.model.chatting.ChatRoomInfo
import com.example.sport_planet.databinding.ActivityBoardBinding
import com.example.sport_planet.presentation.base.BaseActivity
import com.example.sport_planet.presentation.chatting.UserInfo
import com.example.sport_planet.presentation.chatting.view.ChattingActivity
import com.example.sport_planet.presentation.custom.CustomDialog
import com.example.sport_planet.presentation.home.HomeFragment
import com.example.sport_planet.presentation.write.WriteActivity
import com.example.sport_planet.remote.RemoteDataSourceImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.item_custom_toolbar.view.*

class BoardActivity : BaseActivity<ActivityBoardBinding>(R.layout.activity_board) {
    private val viewModel: BoardViewModel by lazy {
        ViewModelProvider(
            this,
            BoardViewModelFactory(RemoteDataSourceImpl())
        ).get(BoardViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.boardId.value = intent.getLongExtra(BOARD_ID, -1)

        viewModel.boardId.observe(this, Observer {
            viewModel.getBoardContent()
        })

        viewModel.successFinish
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                showToast(it)
                setResult(Activity.RESULT_OK)
                finish()
            }
            .addTo(compositeDisposable)

        viewModel.isLoading
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { if (it) showLoading() else hideLoading() }
            .addTo(compositeDisposable)

        viewModel.showBoardHideView
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                showToast("게시물이 피드에서 숨김처리 되었습니다.")
                finish()
            }
            .addTo(compositeDisposable)

        viewModel.boardContent.observe(this, Observer { boardContentModel ->
            val isHost = boardContentModel.host.hostId == UserInfo.USER_ID
            binding.btnChatting.visibility =
                if (isHost) View.GONE else View.VISIBLE

            binding.tvTitle.text = boardContentModel.title
            binding.tvBody.text = boardContentModel.content
            binding.tvPeopleCount.text =
                "남은 인원 ${boardContentModel.recruitNumber - boardContentModel.recruitedNumber}명 (${boardContentModel.recruitedNumber}/${boardContentModel.recruitNumber})"
            binding.tvUserName.text = boardContentModel.host.hostName
            binding.tvGroupStatus.text = boardContentModel.groupStatus.name
            binding.tvExercise.text = boardContentModel.exercise.name
            binding.tvCity.text = boardContentModel.city.name
            binding.tvTag.text = boardContentModel.userTag.name
            val target = boardContentModel.startsAt
            binding.tvDate.text = target.substring(0, 4) + "년 " +
                    target.substring(5, 7) + "월 " +
                    target.substring(8, 10) + "일 " +
                    target.substring(11, 13) + "시 " +
                    target.substring(14, 16) + "분"
            binding.tvPlace.text = boardContentModel.place
            binding.tvLikeCount.text = boardContentModel.host.likes.toString()
            binding.tvDislikeCount.text = boardContentModel.host.dislikes.toString()
            binding.tvHostIntro.text = boardContentModel.host.intro
            binding.toolbar.run {
                this.setMenu(
                    MenuModel(
                        MenuEnum.STAR.apply { this.enabled = boardContentModel.isBookMark },
                        View.OnClickListener { viewModel.bookmarkChange() }),
                    MenuModel(MenuEnum.MENU, View.OnClickListener {
                        this@BoardActivity.let { activity ->
                            val popup = PopupMenu(activity.applicationContext, binding.toolbar.menu)
                            activity.menuInflater.inflate(
                                if (isHost) R.menu.menu_owner_board else R.menu.menu_board,
                                popup.menu
                            )
                            popup.setOnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.board_report -> {
                                        //report
                                        false
                                    }
                                    R.id.board_hide -> {
                                        viewModel.hideBoard()
                                        false
                                    }
                                    R.id.board_edit -> {
                                        WriteActivity.createInstance(
                                            activity = this@BoardActivity,
                                            boardId = boardContentModel.boardId
                                        )
                                        false
                                    }
                                    R.id.board_delete -> {
                                        val dialog = CustomDialog.CustomDialogBuilder()
                                            .setContent(
                                                "삭제 이후에는 다시 복구할 수 없습니다.\n" +
                                                        "정말 삭제하시겠습니까?"
                                            )
                                            .setOKText("삭제하기")
                                            .setOnOkClickedListener {
                                                viewModel.deleteBoard()
                                            }.create()
                                        dialog.show(supportFragmentManager, dialog.tag)
                                        false
                                    }
                                    else -> {
                                        false
                                    }
                                }
                            }
                            popup.show()
                        }
                    })
                )
            }

        })

        binding.toolbar.setSeparator(SeparatorEnum.NONE)
        binding.toolbar.setBackButtonClick(View.OnClickListener { this.finish() })

        binding.btnChatting.setOnClickListener {
            viewModel.makeChattingRoom()
            viewModel.makeChattingRoomResultLiveData.observe(this,
                Observer {
                    it.getContentIfNotHandled()?.data.let { chattingRoom ->
                        if (chattingRoom != null) {
                            val intent = Intent(applicationContext, ChattingActivity::class.java)
                            intent.putExtra(
                                "chatRoomInfo",
                                ChatRoomInfo(
                                    chattingRoom.id,
                                    chattingRoom.boardId,
                                    chattingRoom.guestId,
                                    chattingRoom.hostId == UserInfo.USER_ID,
                                    chattingRoom.opponentNickname
                                )
                            )
                            startActivity(intent)
                        }
                    }
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getBoardContent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HomeFragment.REFRESH) {
            if (resultCode == Activity.RESULT_OK) {
                runOnUiThread {
                    viewModel.getBoardContent()
                }
            }
        }
    }

    companion object {
        const val BOARD_ID = "BOARD_ID"

        fun createInstance(activity: Activity, boardId: Long) {
            val intent = Intent(activity, BoardActivity::class.java)
            intent.putExtra(BOARD_ID, boardId)
            activity.startActivityForResult(intent, HomeFragment.REFRESH)
        }
    }
}