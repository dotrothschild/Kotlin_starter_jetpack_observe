package com.dotrothschild.mysamplesensorswithlivedata.model.data

import com.dotrothschild.mysamplesensorswithlivedata.model.Rank
import kotlinx.coroutines.flow.flowOf

object RankRepository {
    val ranks = flowOf(
        listOf(
            Rank(0,"para0", "para_0",null, ""),
            Rank(1,"para1", "para_1",null, ""),
            Rank(2,"para2", "para_2",null, ""),
            Rank(3,"para3", "para_3",null, ""),
            Rank(4,"para4", "para_4",null, ""),
            Rank(5,"rif0", "rif_1",null, ""),
            Rank(6,"rif1", "rif_2",null, ""),
            Rank(9,"rif2", "rif_3",null, "")
        )
    )
}