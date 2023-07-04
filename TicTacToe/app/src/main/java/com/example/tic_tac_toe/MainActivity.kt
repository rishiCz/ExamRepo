package com.example.tic_tac_toe

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import kotlin.math.log

class MainActivity : AppCompatActivity(),GridListener {
    lateinit var gridView: GridView
    lateinit var mAdapter: GridAdapter
    lateinit var winnerIV: ImageView
    lateinit var winnerTV: TextView
    var isTurn = true
    var isStopped = false
    var isFirstMove = true
    private var gridArr = arrayOf(intArrayOf(0, 0, 0),
                                  intArrayOf(0, 0, 0),
                                  intArrayOf(0, 0, 0))
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resetButton = findViewById<ImageButton>(R.id.resetButton)
        gridView = findViewById<GridView>(R.id.tic_grid)
        mAdapter = GridAdapter(this,this,gridArr)
        winnerIV = findViewById(R.id.winnerIV)
        winnerTV = findViewById(R.id.winnerTV)
        gridView.adapter = mAdapter

        gridView.setOnTouchListener { _, event ->
            event.action == MotionEvent.ACTION_MOVE
        }
        resetButton.setOnClickListener{
            reset()
        }
    }

    override fun onClick(row:Int,col:Int) {
        if(isStopped)
            return
        var current = gridArr[row][col]
        if (current !=0)    return
        if (isTurn) gridArr[row][col] =1
        else gridArr[row][col] =2
        updateGrid()
        if(isWon(gridArr)) {
            winnerIV.setImageResource((if (isTurn) R.drawable.o else R.drawable.x))
            winnerTV.text="Has Won"
            isStopped=true
            return
        }
        if (isDraw()) {
            winnerTV.text="Draw Nobody Looses"
            isStopped=true
            return
        }
        isTurn = !isTurn
        if (!isTurn){
            winnerTV.text="Thinking....."
            cpuMove(row,col)
        }
        else{
            winnerTV.text="Your Turn"
        }
    }

    private fun reset(){
        for (i in 0..2)
            for(j in 0..2)
                gridArr[i][j]=0
        updateGrid()
        winnerIV.setImageResource(0)
        winnerTV.text="Your Turn"
        isFirstMove=true
        isStopped=false
        isTurn=true
    }
    private fun isDraw():Boolean{
        for (i in 0..2)
            for(j in 0..2)
                if(gridArr[i][j]==0)
                    return false
        return true
    }
    private fun isWon(gridArr:Array<IntArray>):Boolean{
        return checkRow(gridArr) || checkColum(gridArr) || checkDiagonals(gridArr)
    }
    private fun checkColum(gridArr:Array<IntArray>):Boolean{
        for (i in 0..2)
        {
            val check = gridArr[0][i]
            if(gridArr[1][i]==check && check ==gridArr[2][i] && check!=0)
                return true
        }
        return false
    }
    private fun checkRow(gridArr:Array<IntArray>):Boolean{
        for (i in 0..2)
        {
            val check = gridArr[i][0]
            if(gridArr[i][1]==check && check ==gridArr[i][2] && check!=0)
                return true
        }
        return false
    }
    private fun checkDiagonals(gridArr:Array<IntArray>):Boolean{
        val isD1 = gridArr[0][0]==gridArr[1][1] && gridArr[1][1]==gridArr[2][2]
        val isD2 = gridArr[0][2]==gridArr[1][1] && gridArr[1][1]==gridArr[2][0]
        return (isD1||isD2) && gridArr[1][1]!=0
    }
    private fun updateGrid() {
        mAdapter.notifyDataSetChanged()
    }

    //-------------Block logic ----------------

    // Start
    // User selects corner      select opp corner
    // User selects center      select corner
    // User selects edgeMid     select center


    // In next move
    // if x(2) is winning    take it
    // if o(1) is winning    block it
    // select first available
    fun cpuMove(row: Int,col: Int){
        if (isFirstMove){
            isFirstMove=false
            firstMove(row,col)
            return
        }
        var xAns= intArrayOf(-1,-1)
        xAns = isWinning(2)
        if(xAns[0]!=-1){
            onClick(xAns[0],xAns[1])
            return
        }
        xAns = isWinning(1)
        if(xAns[0]!=-1){
            onClick(xAns[0],xAns[1])
            return
        }
        for (i in 0..2)
            for(j in 0..2)
                if (gridArr[i][j]==0){
                    onClick(i,j)
                    return
                }

    }
    private fun firstMove(row: Int, col: Int){

        if(isCornerSelected(row,col)){
            onClick(if (row==0) 2 else 0, if (col==0) 2 else 0)
        } else if (isCenterSelected(row,col)){
            onClick(0,0)
        } else {
            onClick(1,1)
        }

    }

    private fun isWinning(player:Int):IntArray{
        val ans = intArrayOf(-1,-1)
        val borad = gridArr.clone()
        for (i in 0..2)
            for(j in 0..2)
            {
                if (borad[i][j]==0) {
                    borad[i][j] = player
                    if (isWon(borad)){
                        ans[0]=i
                        ans[1]=j
                        borad[i][j] =0
                        return ans
                    }
                    borad[i][j] =0
                }
            }
        return ans
    }

    private fun isCornerSelected(row:Int, col:Int):Boolean{
        if((row==0 || row==2) && (col==0 || col==2))    return true
        return false
    }
    private fun isCenterSelected(row:Int, col:Int):Boolean{
        if (row==1 && col==1)   return true
        return false
    }
}