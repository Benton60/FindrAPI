package com.findr.FindrAPI.controller;


import com.findr.FindrAPI.entity.Post;
import com.findr.FindrAPI.service.LocationService;
import com.findr.FindrAPI.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.naming.AuthenticationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping(value = "/createPostWithImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPostWithImage(
            @RequestPart("author") String author,
            @RequestPart("description") String description,
            @RequestPart("longitude") String longitudeStr,
            @RequestPart("latitude") String latitudeStr,
            @RequestPart("image") MultipartFile imageFile) {

        try {
            double longitude = Double.parseDouble(longitudeStr);
            double latitude = Double.parseDouble(latitudeStr);
            Post post = new Post();
            post.setAuthor(author);
            post.setDescription(description);
            post.setLocation(LocationService.createPoint(longitude, latitude));

            //save the post to get the id
            Post createdPost = postService.createPost(post);

            // Build the upload directory path: uploads/{username}/{postId}
            String username = createdPost.getAuthor();
            Long postId = createdPost.getId();

            String uploadDirPath = "C:\\Users\\bento\\IdeaProjects\\FindrAPI\\uploads" + File.separator + username + File.separator + postId;
            File uploadDir = new File(uploadDirPath);

            //if the file doesn't exist and also cant be created then throw an error
            if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                throw new IOException("Could not create upload directory");
            }

            //Save the file with its original filename (or generate one)
            String originalFilename = imageFile.getOriginalFilename();
            if (originalFilename == null) {
                originalFilename = "image.jpg"; // fallback filename
            }

            File savedFile = new File(uploadDir, originalFilename);
            imageFile.transferTo(savedFile);
            System.out.println(savedFile.getAbsolutePath());

            //Update the Post with the image path or URL
            createdPost.setPhotoPath(uploadDirPath + File.separator + originalFilename);
            postService.updatePost(createdPost); // Save changes to the post

            //Return the created post
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);

        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/byID/{id}")
    public ResponseEntity<Post> getPostByID(@PathVariable int id) {
        Post post = postService.findById(id);
        if (post != null) {
            return new ResponseEntity<>(post, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/byAuthor/{page}/{author}")
    public ResponseEntity<List<Post>> getPostByAuthor(@PathVariable int page, @PathVariable String author) {
        List<Post> postList = postService.findByAuthor(page, author);
        return new ResponseEntity<>(postList, HttpStatus.OK); // empty list if no posts
    }
    @GetMapping("/byLocation/{longitude}/{latitude}")
    public ResponseEntity<List<Post>> getPostByLocation(@PathVariable Double longitude, @PathVariable Double latitude) {
        List<Post> postList = postService.findByLocation(LocationService.createPoint(longitude, latitude));
        if (postList != null) {
            return new ResponseEntity<>(postList, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/byPage/{page}/{longitude}/{latitude}")
    public ResponseEntity<List<Post>> getPostByPage(@PathVariable int page, @PathVariable Double longitude, @PathVariable Double latitude) {
        List<Post> postList = postService.findByPage(page, LocationService.createPoint(longitude, latitude));
        return new ResponseEntity<>(postList, HttpStatus.OK); // empty list if no posts
    }

}
