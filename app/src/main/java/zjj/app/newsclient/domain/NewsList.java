/*
 * Copyright (c) 2016.
 * Jason Zhang
 * All Rights Reserved
 */

package zjj.app.newsclient.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class NewsList {


    private int showapi_res_code;
    private String showapi_res_error;

    private ShowapiResBodyBean showapi_res_body;

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public ShowapiResBodyBean getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ShowapiResBodyBean showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public static class ShowapiResBodyBean {
        private int ret_code;

        private PagebeanBean pagebean;

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public PagebeanBean getPagebean() {
            return pagebean;
        }

        public void setPagebean(PagebeanBean pagebean) {
            this.pagebean = pagebean;
        }

        @Override
        public String toString() {
            return "ShowapiResBodyBean{" +
                    "ret_code=" + ret_code +
                    ", pagebean=" + pagebean +
                    '}';
        }

        public static class PagebeanBean {
            private int allPages;
            private int currentPage;
            private int allNum;
            private int maxResult;

            private List<ContentlistBean> contentlist;

            public int getAllPages() {
                return allPages;
            }

            public void setAllPages(int allPages) {
                this.allPages = allPages;
            }

            public int getCurrentPage() {
                return currentPage;
            }

            public void setCurrentPage(int currentPage) {
                this.currentPage = currentPage;
            }

            public int getAllNum() {
                return allNum;
            }

            public void setAllNum(int allNum) {
                this.allNum = allNum;
            }

            public int getMaxResult() {
                return maxResult;
            }

            public void setMaxResult(int maxResult) {
                this.maxResult = maxResult;
            }

            public List<ContentlistBean> getContentlist() {
                return contentlist;
            }

            public void setContentlist(List<ContentlistBean> contentlist) {
                this.contentlist = contentlist;
            }

            @Override
            public String toString() {
                return "PagebeanBean{" +
                        "allPages=" + allPages +
                        ", currentPage=" + currentPage +
                        ", allNum=" + allNum +
                        ", maxResult=" + maxResult +
                        ", contentlist=" + contentlist +
                        '}';
            }

            public static class ContentlistBean implements Parcelable {
                private String pubDate;
                private String title;
                private String channelName;
                private String desc;
                private String source;
                private String channelId;
                private String link;
                private String html;

                private List<ImageurlsBean> imageurls;

                public String getPubDate() {
                    return pubDate;
                }

                public void setPubDate(String pubDate) {
                    this.pubDate = pubDate;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getChannelName() {
                    return channelName;
                }

                public void setChannelName(String channelName) {
                    this.channelName = channelName;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public String getSource() {
                    return source;
                }

                public void setSource(String source) {
                    this.source = source;
                }

                public String getChannelId() {
                    return channelId;
                }

                public void setChannelId(String channelId) {
                    this.channelId = channelId;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getHtml() {
                    return html;
                }

                public void setHtml(String html) {
                    this.html = html;
                }

                public List<ImageurlsBean> getImageurls() {
                    return imageurls;
                }

                public void setImageurls(List<ImageurlsBean> imageurls) {
                    this.imageurls = imageurls;
                }

                @Override
                public String toString() {
                    return "ContentlistBean{" +
                            "pubDate='" + pubDate + '\'' +
                            ", title='" + title + '\'' +
                            ", channelName='" + channelName + '\'' +
                            ", desc='" + desc + '\'' +
                            ", source='" + source + '\'' +
                            ", channelId='" + channelId + '\'' +
                            ", link='" + link + '\'' +
                            ", html='" + html + '\'' +
                            ", imageurls=" + imageurls +
                            '}';
                }

                public static class ImageurlsBean implements Parcelable {

                    private int height;
                    private int width;
                    private String url;

                    public int getHeight() {
                        return height;
                    }

                    public void setHeight(int height) {
                        this.height = height;
                    }

                    public int getWidth() {
                        return width;
                    }

                    public void setWidth(int width) {
                        this.width = width;
                    }

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeInt(this.height);
                        dest.writeInt(this.width);
                        dest.writeString(this.url);
                    }

                    public ImageurlsBean() {
                    }

                    protected ImageurlsBean(Parcel in) {
                        this.height = in.readInt();
                        this.width = in.readInt();
                        this.url = in.readString();
                    }

                    public static final Creator<ImageurlsBean> CREATOR = new Creator<ImageurlsBean>() {
                        @Override
                        public ImageurlsBean createFromParcel(Parcel source) {
                            return new ImageurlsBean(source);
                        }

                        @Override
                        public ImageurlsBean[] newArray(int size) {
                            return new ImageurlsBean[size];
                        }
                    };
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.pubDate);
                    dest.writeString(this.title);
                    dest.writeString(this.channelName);
                    dest.writeString(this.desc);
                    dest.writeString(this.source);
                    dest.writeString(this.channelId);
                    dest.writeString(this.link);
                    dest.writeString(this.html);
                    dest.writeList(this.imageurls);
                }

                public ContentlistBean() {
                }

                protected ContentlistBean(Parcel in) {
                    this.pubDate = in.readString();
                    this.title = in.readString();
                    this.channelName = in.readString();
                    this.desc = in.readString();
                    this.source = in.readString();
                    this.channelId = in.readString();
                    this.link = in.readString();
                    this.html = in.readString();
                    this.imageurls = new ArrayList<ImageurlsBean>();
                    in.readList(this.imageurls, ImageurlsBean.class.getClassLoader());
                }

                public static final Parcelable.Creator<ContentlistBean> CREATOR = new Parcelable.Creator<ContentlistBean>() {
                    @Override
                    public ContentlistBean createFromParcel(Parcel source) {
                        return new ContentlistBean(source);
                    }

                    @Override
                    public ContentlistBean[] newArray(int size) {
                        return new ContentlistBean[size];
                    }
                };
            }
        }
    }


}
