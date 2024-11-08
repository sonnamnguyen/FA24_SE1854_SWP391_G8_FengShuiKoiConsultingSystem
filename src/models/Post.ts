import { Account } from './Account';
import { PostCategory } from './PostCategory';
import { Package } from './Package';
import { Destiny } from './Destiny';
import { Status } from './enums/Status';
import { Comment } from './Comment';
import { PostImage } from './PostImage';

export class Post {
    postId: number;
    account: Account | null;
    postCategory: PostCategory | null;
    packageId: Package | null;
    destiny: Destiny | null;
    content: string;
    likeNumber: number;
    dislikeNumber: number;
    shareNumber: number;
    status: Status;
    createdDate: Date;
    title: string;
    createdBy: string;
    updatedDate: Date | null;
    updatedBy: string | null;
    comments: Set<Comment>;
    images: Set<PostImage>;

    constructor() {
        this.postId = 0;
        this.account = null;
        this.postCategory = null;
        this.packageId = null;
        this.destiny = null;
        this.content = '';
        this.likeNumber = 0;
        this.dislikeNumber = 0;
        this.shareNumber = 0;
        this.status = Status.INACTIVE;
        this.createdDate = new Date();
        this.title = '';
        this.createdBy = '';
        this.updatedDate = null;
        this.updatedBy = null;
        this.comments = new Set<Comment>();
        this.images = new Set<PostImage>();
    }
}
