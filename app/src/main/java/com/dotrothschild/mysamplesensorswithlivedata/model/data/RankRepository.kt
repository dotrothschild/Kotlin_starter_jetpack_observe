package com.dotrothschild.mysamplesensorswithlivedata.model.data

import com.dotrothschild.mysamplesensorswithlivedata.model.Rank
import kotlinx.coroutines.flow.flowOf

object RankRepository {
    val ranks = flowOf(
        listOf(
            Rank(0,"Recruit", "x",null, ""),
            Rank(1,"a", "a",null, ""),
            Rank(2,"b", "b",null, ""),
            Rank(3,"c", "c",null, ""),
            Rank(4,"d", "d",null, ""),
            Rank(5,"e", "e",null, ""),
            Rank(6,"f", "f",null, ""),
            Rank(7,"g", "g",null, ""),
            Rank(8,"h", "h",null, ""),
            Rank(9,"i", "i",null, "")
        )
    )
}