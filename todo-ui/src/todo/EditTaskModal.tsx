import React, { useState } from 'react';
import { Modal, Box, TextField, Button, Chip } from '@mui/material';
import {TaskDto, UpdateTaskDto} from "../generated/models";

interface EditTaskModalProps {
    task: TaskDto;
    isOpen: boolean;
    onClose: () => void;
    onSave: (updatedTask: UpdateTaskDto) => void;
}

const EditTaskModal: React.FC<EditTaskModalProps> = ({ task, isOpen, onClose, onSave }) => {
    const [title, setTitle] = useState(task.title);
    const [description, setDescription] = useState(task.description || '');
    const [tags, setTags] = useState<string[]>(task.tags);
    const [newTag, setNewTag] = useState('');

    const handleSave = () => {
        onSave({ title, description, tags });
        onClose();
    };

    const handleAddTag = () => {
        if (newTag.trim()) {
            setTags([...tags, newTag.trim()]);
            setNewTag('');
        }
    };

    const handleDeleteTag = (tagToDelete: string) => {
        setTags(tags.filter((tag) => tag !== tagToDelete));
    };

    return (
        <Modal open={isOpen} onClose={onClose}>
            <Box sx={{ p: 4, backgroundColor: 'white', borderRadius: 1, width: 400, margin: 'auto', mt: 8 }}>
                <TextField
                    fullWidth
                    label="Title"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    margin="normal"
                />
                <TextField
                    fullWidth
                    label="Description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    margin="normal"
                />
                <Box sx={{ display: 'flex', alignItems: 'center', mt: 2 }}>
                    <TextField
                        label="Add Tag"
                        value={newTag}
                        onChange={(e) => setNewTag(e.target.value)}
                        margin="normal"
                    />
                    <Button onClick={handleAddTag} sx={{ ml: 2 }}>
                        Add
                    </Button>
                </Box>
                <Box sx={{ display: 'flex', flexWrap: 'wrap', mt: 2 }}>
                    {tags.map((tag) => (
                        <Chip
                            key={tag}
                            label={tag}
                            onDelete={() => handleDeleteTag(tag)}
                            sx={{ mr: 0.5, mb: 0.5 }}
                        />
                    ))}
                </Box>
                <Box sx={{ mt: 4, display: 'flex', justifyContent: 'flex-end' }}>
                    <Button onClick={onClose} sx={{ mr: 2 }}>Cancel</Button>
                    <Button variant="contained" onClick={handleSave}>Save</Button>
                </Box>
            </Box>
        </Modal>
    );
};

export default EditTaskModal;
