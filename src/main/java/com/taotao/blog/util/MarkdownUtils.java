package com.taotao.blog.util;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.*;

/**
 * @author Taotao Ma
 */
public class MarkdownUtils {

    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node node = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(node);
    }

    public static String markdownToHtmlExtensions(String markdown) {
        // heading set id
        Set<Extension> headingExtensions = Collections.singleton(HeadingAnchorExtension.create());
        // table convert
        List<Extension> tableExtensions = Collections.singletonList(TablesExtension.create());

        Parser parser = Parser.builder()
                .extensions(tableExtensions)
                .build();
        Node node = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingExtensions)
                .extensions(tableExtensions)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    @Override
                    public AttributeProvider create(AttributeProviderContext attributeProviderContext) {
                        return new CustomAttributeProvider();
                    }
                })
                .build();
        return renderer.render(node);
    }

    static class CustomAttributeProvider implements AttributeProvider {

        @Override
        public void setAttributes(Node node, String s, Map<String, String> map) {
            if (node instanceof Link) {
                map.put("target", "_blank");
            }
            if (node instanceof TableBlock) {
                map.put("class", "ui celled table");
            }
        }
    }

}
