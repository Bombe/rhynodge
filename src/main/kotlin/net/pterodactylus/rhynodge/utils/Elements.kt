package net.pterodactylus.rhynodge.utils

import org.jsoup.nodes.Element

fun Element.sourceAttribute() = attr("src")
