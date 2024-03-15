package com.example.studybuddyai.algorithm

class Algorithm {

    fun calculateMarks(incorrectAnswer: String, correctAnswer: String): Int {
        val maxLength = maxOf(incorrectAnswer.length, correctAnswer.length)
        val distance = levenshteinDistance(incorrectAnswer, correctAnswer)
        val similarityPercentage = ((maxLength - distance) / maxLength.toDouble()) * 100

        // Scale the similarity percentage to marks out of 10
        val marks = (similarityPercentage / 100) * 10
        return marks.toInt()
    }

    private fun levenshteinDistance(s1: String, s2: String): Int {
        val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }

        for (i in 0..s1.length) {
            for (j in 0..s2.length) {
                when {
                    i == 0 -> dp[i][j] = j
                    j == 0 -> dp[i][j] = i
                    else -> {
                        dp[i][j] = minOf(
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1,
                            dp[i - 1][j - 1] + if (s1[i - 1] == s2[j - 1]) 0 else 1
                        )
                    }
                }
            }
        }
        return dp[s1.length][s2.length]
    }
}