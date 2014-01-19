#include "file_cache_impl.h"

#include <fstream>
#include <stdio.h>
//using namespace std;
#define MAX_FILE_SIZE 1024
FileCacheImpl::FileCacheImpl(int max_cache_entries, int dirty_time_secs)
		: FileCache(max_cache_entries, dirty_time_secs), _stopped(false)
{
	pthread_mutex_init(&_lock, NULL);
	pthread_cond_init(&_cond, NULL);
	pthread_create(&bg_t, NULL, bg_thread_func, this);
}

FileCacheImpl::~FileCacheImpl()
{}

void
FileCacheImpl::PinFiles(const std::vector<std::string>& file_vec)
{
	int i = 0;
	for (i = 0; i < file_vec.size(); i++) {
		pin_file(file_vec[i]);
	}
}

void
FileCacheImpl::pin_file(const std::string& file_name)
{
	pthread_mutex_lock(&_lock);
	std::map<std::string, FileCacheEntry*>::iterator iter = _cache.find(file_name);
	// if the file is already in cache, just increment the ref count.
	if (iter != _cache.end()) {
		FileCacheEntry *c_entry = iter->second;
		c_entry->increment_ref_count();
		pthread_mutex_unlock(&_lock);
		return;
	}
	FileCacheEntry *c_entry = get_cache_entry(file_name);
	// no free cache entry and all files are pinned,
	// just block until some of them to be unpinned.
	if (c_entry == NULL) {
		pthread_cond_wait(&_cond, &_lock);
		pthread_mutex_unlock(&_lock);
		pin_file(file_name);
		return;
	}
	std::fstream fin;
	fin.open(file_name, ios_base::in);
	if (fin.is_open()) {
		fin.read(c_entry->get_writable_buffer(), MAX_FILE_SIZE);
		fin.close();
	} else {
		fin.clear();
		fin.open(file_name, ios_base::out);
		fin.write(c_entry->get_readonly_buffer(), MAX_FILE_SIZE);
		fin.close();
	}
	c_entry->increment_ref_count();

	pthread_mutex_unlock(&_lock);
}

FileCacheEntry *
FileCacheImpl::get_cache_entry(const std::string& file_name)
{
	if (_cache.size() < max_cache_entries_)
	{
		FileCacheEntry *c_entry = new FileCacheEntry(file_name, MAX_FILE_SIZE);
		_cache[file_name] = c_entry;
		return c_entry;

	}
	if (evict_an_entry() == 0) {
		FileCacheEntry *c_entry = new FileCacheEntry(file_name, MAX_FILE_SIZE);
		_cache[file_name] = c_entry;
		return c_entry;
	}
	return NULL;
}

int
FileCacheImpl::evict_an_entry()
{
	int ret = -1;
	std::map<std::string, FileCacheEntry *>::iterator iter;
	for (iter = _cache.begin(); iter != _cache.end(); iter++) {
		FileCacheEntry *c_entry = iter->second;
		if (c_entry->get_ref_count() == 0 && !c_entry->is_dirty()) {
			_cache.erase(iter);
			delete(c_entry);
			ret = 0;
			break;
		}
	}
	return ret;
}

void
FileCacheImpl::UnpinFiles(const std::vector<std::string>& file_vec)
{
	int i = 0;
	for (i = 0; i < file_vec.size(); i++) {
		unpin_file(file_vec[i]);
	}
}

void
FileCacheImpl::unpin_file(const std::string& file_name)
{
	pthread_mutex_lock(&_lock);
	std::map<std::string, FileCacheEntry*>::iterator iter = _cache.find(file_name);
	if (iter != _cache.end()) {
		FileCacheEntry *c_entry = iter->second;
		c_entry->decrement_ref_count();
		if (c_entry->get_ref_count() == 0 &&
		    c_entry->is_delete()) {
			//remove(file_name);
			_cache.erase(iter);
			delete(c_entry);
		}
	}
	pthread_mutex_unlock(&_lock);
	return;
}

void
FileCacheImpl::DeleteFile(const std::string& file_name)
{
	pthread_mutex_lock(&_lock);
	std::map<std::string, FileCacheEntry*>::iterator iter = _cache.find(file_name);
	if (iter != _cache.end()) {
		FileCacheEntry *c_entry = iter->second;
		if (c_entry->get_ref_count() == 0) {
			//remove(file_name);
			_cache.erase(iter);
			delete(c_entry);
		// TODO , delete the file.
		} else
		{
			c_entry->mark_delete();
		}
	} else {
	//	remove(file_name);
	// TODO the file is not in the cache, delete the file directly.
	}
	pthread_mutex_unlock(&_lock);

}

const char *
FileCacheImpl::FileData(const std::string& file_name)
{
	const char *ret = NULL;
	pthread_mutex_lock(&_lock);
	std::map<std::string, FileCacheEntry *>::iterator iter = _cache.find(file_name);
	if (iter != _cache.end()) {
		FileCacheEntry *c_entry = iter->second;
		ret = c_entry->get_readonly_buffer();
	}
	pthread_mutex_unlock(&_lock);
	return ret;
}

char *
FileCacheImpl::MutableFileData(const std::string& file_name)
{
	char *ret = NULL;
	pthread_mutex_lock(&_lock);
	std::map<std::string, FileCacheEntry *>::iterator iter = _cache.find(file_name);
	if (iter != _cache.end()) {
		FileCacheEntry *c_entry = iter->second;
		ret = c_entry->get_writable_buffer();
	}
	pthread_mutex_unlock(&_lock);
	return ret;
}

void *
FileCacheImpl::bg_thread()
{
	while(true) {
		sleep(dirty_time_secs_ * 1000);
		pthread_mutex_lock(&_lock);
		std::map<std::string, FileCacheEntry *>::iterator iter;
		for (iter = _cache.begin(); iter != _cache.end(); iter++) {
            std::string file_name = iter->first;
			FileCacheEntry *c_entry = iter->second;
			time_t cur_time;
			time(&cur_time);
			if ( (_stopped == true && c_entry->is_dirty()) ||
			     (c_entry->get_ref_count() == 0 && c_entry->is_dirty() &&
			      cur_time - c_entry->get_dirty_start_time() >= dirty_time_secs_)) {
				// write the content to the disk and mark the cache as clean
				std::fstream fout;
				fout.open(file_name, ios_base::out);
				fout.write(c_entry->get_readonly_buffer(), MAX_FILE_SIZE);
				fout.close();
				c_entry->mark_clean();
			}
		}
		pthread_cond_broadcast(&_cond);
		if (_stopped) {
			break;
		}
		pthread_mutex_unlock(&_lock);

	}
}
int main()
{
}
