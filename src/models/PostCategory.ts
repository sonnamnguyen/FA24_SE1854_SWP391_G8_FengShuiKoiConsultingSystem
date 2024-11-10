import { Status } from './enums/Status';
import { Post } from './Post';

export class PostCategory {
    postCategoryId: number;
    postCategoryName: string;
    status: Status;
    posts: Set<Post>;

    constructor() {
        this.postCategoryId = 0;
        this.postCategoryName = '';
        this.status = Status.INACTIVE;
        this.posts = new Set<Post>();
    }
}
