package com.lazytravel.ui.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Simple HTML-to-Compose renderer for blog content
 * Supports: h1, h2, h3, p, strong, em, ul, li, ol, br
 */
@Composable
fun HtmlContent(
    html: String,
    modifier: Modifier = Modifier
) {
    val lines = html.split("\n")
    Column(modifier = modifier.fillMaxWidth()) {
        var inUL = false
        var inOL = false
        var olCounter = 1

        for (line in lines) {
            val trimmed = line.trim()

            when {
                trimmed.startsWith("<h1>") -> {
                    val text = extractText(trimmed, "<h1>", "</h1>")
                    HtmlHeading1(text)
                }
                trimmed.startsWith("<h2>") -> {
                    val text = extractText(trimmed, "<h2>", "</h2>")
                    HtmlHeading2(text)
                }
                trimmed.startsWith("<h3>") -> {
                    val text = extractText(trimmed, "<h3>", "</h3>")
                    HtmlHeading3(text)
                }
                trimmed.startsWith("<p>") -> {
                    val text = extractText(trimmed, "<p>", "</p>")
                    HtmlParagraph(text)
                }
                trimmed.startsWith("<ul>") -> {
                    inUL = true
                }
                trimmed.startsWith("</ul>") -> {
                    inUL = false
                }
                trimmed.startsWith("<ol>") -> {
                    inOL = true
                    olCounter = 1
                }
                trimmed.startsWith("</ol>") -> {
                    inOL = false
                }
                trimmed.startsWith("<li>") && inUL -> {
                    val text = extractText(trimmed, "<li>", "</li>")
                    HtmlListItem(text, isBullet = true)
                }
                trimmed.startsWith("<li>") && inOL -> {
                    val text = extractText(trimmed, "<li>", "</li>")
                    HtmlListItem(text, isBullet = false, number = olCounter)
                    olCounter++
                }
                trimmed.startsWith("<br>") || trimmed == "<br/>" -> {
                    // Add spacing for line breaks
                    Text("")
                }
            }
        }
    }
}

@Composable
private fun HtmlHeading1(text: String) {
    Text(
        text = parseInlineHtml(text),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF212121),
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
private fun HtmlHeading2(text: String) {
    Text(
        text = parseInlineHtml(text),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF212121),
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 0.dp)
    )
}

@Composable
private fun HtmlHeading3(text: String) {
    Text(
        text = parseInlineHtml(text),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF424242),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun HtmlParagraph(text: String) {
    Text(
        text = parseInlineHtml(text),
        style = MaterialTheme.typography.bodyMedium,
        color = Color(0xFF424242),
        modifier = Modifier.padding(vertical = 8.dp),
        lineHeight = 22.sp
    )
}

@Composable
private fun HtmlListItem(text: String, isBullet: Boolean = true, number: Int = 0) {
    val prefix = if (isBullet) "• " else "$number. "
    Text(
        text = prefix + parseInlineHtml(text),
        style = MaterialTheme.typography.bodyMedium,
        color = Color(0xFF424242),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        lineHeight = 20.sp
    )
}

private fun parseInlineHtml(text: String): androidx.compose.ui.text.AnnotatedString {
    return buildAnnotatedString {
        var remaining = text
        var index = 0

        while (index < remaining.length) {
            // Look for HTML tags
            val nextTag = remaining.indexOf("<", index)

            if (nextTag == -1) {
                // No more tags, append the rest
                append(remaining.substring(index).replace("&nbsp;", " "))
                break
            }

            // Append text before the tag
            if (nextTag > index) {
                append(remaining.substring(index, nextTag).replace("&nbsp;", " "))
            }

            // Find the end of the tag
            val tagEnd = remaining.indexOf(">", nextTag)
            if (tagEnd == -1) {
                append(remaining.substring(nextTag))
                break
            }

            val tag = remaining.substring(nextTag, tagEnd + 1)

            when {
                tag.startsWith("<strong>") -> {
                    // Find closing tag
                    val closeIndex = remaining.indexOf("</strong>", tagEnd)
                    if (closeIndex != -1) {
                        val content = remaining.substring(tagEnd + 1, closeIndex)
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(content)
                        }
                        index = closeIndex + "</strong>".length
                    } else {
                        index = tagEnd + 1
                    }
                }
                tag.startsWith("<b>") -> {
                    val closeIndex = remaining.indexOf("</b>", tagEnd)
                    if (closeIndex != -1) {
                        val content = remaining.substring(tagEnd + 1, closeIndex)
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(content)
                        }
                        index = closeIndex + "</b>".length
                    } else {
                        index = tagEnd + 1
                    }
                }
                tag.startsWith("<em>") -> {
                    val closeIndex = remaining.indexOf("</em>", tagEnd)
                    if (closeIndex != -1) {
                        val content = remaining.substring(tagEnd + 1, closeIndex)
                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(content)
                        }
                        index = closeIndex + "</em>".length
                    } else {
                        index = tagEnd + 1
                    }
                }
                tag.startsWith("<i>") -> {
                    val closeIndex = remaining.indexOf("</i>", tagEnd)
                    if (closeIndex != -1) {
                        val content = remaining.substring(tagEnd + 1, closeIndex)
                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(content)
                        }
                        index = closeIndex + "</i>".length
                    } else {
                        index = tagEnd + 1
                    }
                }
                else -> {
                    // Skip unknown tags
                    index = tagEnd + 1
                }
            }
        }
    }
}

private fun extractText(html: String, openTag: String, closeTag: String): String {
    val start = html.indexOf(openTag) + openTag.length
    val end = html.indexOf(closeTag)
    return if (start > openTag.length - 1 && end > start) {
        html.substring(start, end)
    } else {
        html
    }
}
